import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import e4m.io.SnapOutputStream;
import e4m.net.telnet.TelnetConnection;
import e4m.net.tn3270.Tn3270Connection;
import e4m.net.tn3270.datastream.Viewport;
import e4m.net.tn3270.datastream.Text;
import e4m.net.tn3270.ds.Partition;
import e4m.js.trace.TcpdumpPacket;
import e4m.ui.html.Html;

public class Sim extends Prt {
  public static void main(String[] a) throws Exception {
    new Sim().start(a);
  }
  
  URLConnection uc;
  OutputStream out;
  
  String src;
  Html html;
  int fileno;
  
  void start(String[] a) throws Exception {
    src = a[0];
    String url = a[1];
    html = new Html( Integer.parseInt(a[2]),   // rows
                     Integer.parseInt(a[3]) ); // cols
    ebcdic("Cp037");
    html.setCodec(codec);
    fileno = 0;
    
    if (a.length > 4) max = Integer.parseInt(a[4]);

    tcpdump(System.in);
    TcpdumpPacket p;
    while ((p = trc.readPacket()) != null) {
      log("p: "+p.timestamp()+' '+p.source()+' '+p.destination()+' '+p.flags()+' '+p.datalength());
      if (p.flags().equals("S")) {
        if (p.source().equals(src)) {
          connect(url);
        }
      }  
      else
        if (p.datalength() > 0 && p.data()[0] != Prt.IAC) {
          if (p.source().equals(src)) {
            source(p);
          } else {
            destination(p);
          }
        }      
    }
    
    trc.close();
    prt.close();
  }
  
  void connect(String u) throws Exception {
    System.setProperty("java.protocol.handler.pkgs","e4m.net");
    System.setProperty("java.content.handler.pkgs","e4m.net");

    URL url = new URL(u);
    uc = url.openConnection();

    log("port: "+url.getDefaultPort());
    log("url: "+uc.getURL().toExternalForm());
    log("dev: "+uc.getRequestProperty("Device-Type"));

    uc.connect();
    out = ((TelnetConnection)uc).getDirectOutputStream();
  }
  
  SnapOutputStream dump = new SnapOutputStream(new OutputStreamWriter(System.err),"Cp037");
  
  void dump(byte[] b, int off, int len) throws IOException {
    dump.write(b,off,len);
    dump.close();
  }
  
  void destination(TcpdumpPacket p) throws Exception {
    log("received: "+p.datalength());
    byte[] b = p.data();
    for (int i = 0; i < b.length; i++) {
      if (b[i] == Prt.IAC) {
        i++;
        if (b[i] == Prt.EOR) {
          receive(p.data());
        }
      }
    }
  }
  
  void source(TcpdumpPacket p) throws Exception {
    byte[] b = p.data();
    if (b[0] == 0x02) return;
    transmit(b);
    log("sent: "+p.datalength());
    dump(b,0,b.length);
  }

  void transmit(byte[] b) throws Exception {
    out.write(b);
  }

  int max = 7;
  int min(int a, int b) {
    return (a < b) ? a : b;
  }
  
  void receive(byte[] b) throws Exception {
    uc.getInputStream().reset();
    Viewport v = (Viewport) uc.getContent();
    ByteBuffer buf = v.datastream();

    buf.position(0);
    log("packet: "+buf.remaining());
    dump(buf.array(),buf.position(),min(max,buf.limit()));

    byte[] header = v.header();
    ack(header);

    if (v.command() > 0) {
      int seq = ((header[3] & 0x0ff) << 8) | (header[4] & 0x0ff);
      System.err.println("parse: "+seq+" cmd:"+Integer.toHexString(v.command() & 0x0ff)
                                      + " kb:"+v.restoreKeyboard()
                                      +" mdt:"+v.resetModifiedDataTags());
      if (v.fields() != null) {
        print(v);
      }
    }
  }

  void ack(byte[] b) throws IOException {
    if (b[2] == 0x02) {
      ((Tn3270Connection)uc).writeBytes( 0x002, 0x000, 0x000, b[3], b[4],
                                         0x000, 0x0ff, 0x0ef );
    }
  }

  void print(Viewport v) throws Exception {
    fileno++;
    File f = new File("htm/t"+fileno+".html");
    OutputStream o = new FileOutputStream(f);

    printout(o);
    Text t = v.fields();
    
    print(   "<html>"
      +'\n'+  "<head>"
      +'\n'+   "<style type='text/css'>"
      +'\n');    stylesheet();
    print(     "</style>"
      +'\n'+  "</head>"
      +'\n'+  "<body>"
      +'\n'+   "<form action='t"+(fileno+1)+".html'>"
               +"<input type='submit' value='next'>"
      +'\n');    form(t);
    print(     "</form>"
      +'\n'+  "</body>"
      +'\n'+  "<!-- Buffer"
      +'\n');       buffer(t.text());
    print(    "-->"
      +'\n'+  "<!-- Fields"
      +'\n');       fields(t,v.cursor());
    print(    "-->"
      +'\n'+  "<!-- Layout"
      +'\n');       layout();
    print(    "-->"
      +'\n'+ "</html>"
      +'\n');

    prt.flush();
    o.close();

    log("file: "+f.getCanonicalPath());
  }
  
  void stylesheet() { println(html.stylesheet().toString()); }
  void form(Text t) { println(html.parse(t).toString()); }
  void fields(Text t, int c) { print((Partition.PresentationSpace)t,c); }
  void layout() { print(html,html.height(),html.width()); }
  
  void buffer(ByteBuffer b) throws Exception {
    b.position(0);
    print(b,"Cp037");
  }
}
