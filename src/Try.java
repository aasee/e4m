import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import e4m.io.ByteCollector;
import e4m.net.Vty.AttentionListener;
import e4m.net.tn3270.datastream.Viewport;
import e4m.net.tn3270.ds.Partition;
import e4m.ui.swt.Tty;

public class Try extends Prt {
  public static void main(String[] a) throws Exception {
    new Try().start(a);
  }

  Partition emu;
  Tty scr;
  
  Thread main;
  
  void start(String[] a) throws Exception {
    tcpdump(a[0]);
    printout(System.out);
    ebcdic("Cp037");
    
    main = Thread.currentThread();
    
    int rows = Integer.parseInt(a[1]);
    int cols = Integer.parseInt(a[2]);
    
    emu = new Partition();
    emu.setSize(rows,cols);
    
    scr = new Tty();
    scr.start();
    scr.setSize(rows,cols);
    scr.setCodec(codec);   
    scr.setAttentionListener(attn);
    
    ByteBuffer b = getBuf();
    if (b.hasRemaining()) {     
      print(b,"Cp037");
      println();
      
      b.position(5);
      emu.write(b);
      Partition.State p = emu.getState();
      Viewport f = emu.getViewport();
      
      print((Partition.PresentationSpace)f,p.cursor);
      println();
      
      ByteBuffer t = f.text();
      print(t,"Cp037");
      println();
      
      t.flip();
      CharBuffer c = Charset.forName("Cp037").decode(t);
      print(c,rows,cols);
      println();
      
      char[] z = new char[rows*cols];
      Arrays.fill(z,' ');
      c = CharBuffer.wrap(z);
      codec.decode(f,c);
      c.position(0);
      print(c,rows,cols);
      println();
      
//    Html html = new Html(24,80);
//    html.setCodec(codec);
//    println(html.parse(f).toString());
//    prt.flush();
      
      scr.update(p.command,p.cursor,f);
      
      System.err.println("main: "+Thread.currentThread().toString());
      
      sync();      
    }
    
    scr.close();
    System.err.println("done:");
  }
  
  void sync() {
    synchronized(main) {
      try { main.wait(); }
        catch (InterruptedException ie) { System.err.println("burp:"); }
    }  
  }
  
  ByteBuffer getBuf() throws Exception { 
    ByteCollector b = new ByteCollector();
    byte[] data = null;
    int i = 0;
    for (;;) {
      if (data == null || i >= data.length) {
        data = trc.readPacket().data();
        i = 0;
      }
      if (data[i] == IAC) {
        i++;
        if (data[i] == EOR) break;
        if (data[i] != IAC) throw new IOException("invalid character <"+Integer.toHexString(data[i])+'>');
      }  
      b.write(data[i]);  
      i++;
    }
    return b.toByteBuffer();
  }

  AttentionListener attn = new AttentionListener() {
    @Override
    public void attention(int aid, int cursor, Viewport fields) {
      System.err.println("att: "+Thread.currentThread().toString()
                                              +' '+aid+' '+cursor);
      for (int i = 0; i < fields.count(); i++) {
        if (fields.isModified(i)) {
          byte[] b = new byte[fields.length(i)];
          int start = fields.start(i);
          fields.text().position(start); 
          fields.text().get(b);
          try {
            System.err.println("mod: "+i+' '+start
                                        +' '+fields.end(i)
                                        +" '"+(new String(b,"Cp037"))+"' "+b.length);
            print(fields.text(),"Cp037");
          }
          catch (Exception e) {
            e.printStackTrace();
          } 
        }
      }
      main.interrupt();
    }
  };

}