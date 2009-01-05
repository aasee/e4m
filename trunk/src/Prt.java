
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Formatter;
import java.util.Locale;

import e4m.io.SnapOutputStream;
import e4m.net.tn3270.ds.Partition;
import e4m.tcp.trace.TcpdumpReader;
import e4m.ui.Codec;
import e4m.ui.SimpleCodec;

public class Prt {

  static int IAC = (byte) 0xff;
  static int EOR = (byte) 0xef;
  
  TcpdumpReader trc;     // input.trc
  PrintWriter prt;       // System.out
  SnapOutputStream snap; // SnapOutputStream(log);
  Codec codec;
  
  void ebcdic(String charsetName) {
    codec = new SimpleCodec(charsetName);
  }
   
  void tcpdump(String filename) throws FileNotFoundException {
    trc = new TcpdumpReader(new FileReader(filename));
  }
  
  void printout(OutputStream print) {
    this.prt = new PrintWriter(print);
    this.snap = new SnapOutputStream(prt);
  }

  void log(String s) { System.err.println(s); }
  
  void println() { prt.println(); }
  void println(String s) { prt.println(s); }
  
  void print(String s) { prt.print(s); }
  
  void print(CharSequence c, int y, int x) {
    String s = c.toString().replace((char)0,' ');
    for (int i=0, j=x; i < y*x; i = j, j += x) {
      prt.println("|"+s.substring(i,j)+'|');
    }
    prt.flush();
  }
  
  void print(ByteBuffer b, String charsetName) throws Exception {
    SnapOutputStream o = new SnapOutputStream(prt,charsetName);
    while (b.hasRemaining()) o.write(b.get());
    o.close();
  }
  
  String b28 = "                            ";
  StringBuilder sb = new StringBuilder();
  Formatter hdr = new Formatter(sb,Locale.getDefault());
  
  void print(Partition.PresentationSpace v, int cursor) {
    prt.println("cursor: "+cursor);
    
    for (int i = 0; i < v.count(); i++) {
      int start = v.start(i);
      int end = v.end(i);
      long attributes = v.attributes(i);
      byte[] buf = v.text().array();

      sb.setLength(0);
      String fid = hdr.format( "%1$4d. %2$4d %3$4d %4$10x -",
                                 i,    start, end,  attributes ).toString();

      int z; for (z = start; z < end && buf[z] == 0; z++ ) {}

      if (z < end) {
        while (start < end) {
          int limit = start + 16;
          if (limit > end) limit = end;
          prt.println(fid+toHex(buf,start,limit));
          fid = b28;
          start = limit;
        }
      }
      else {
        prt.println(fid+" .. "+(end - start));
      }
    }
    prt.flush();
  }
  
  CharSequence toHex(byte[] b, int start, int end) {
    StringBuilder buf = new StringBuilder();
    for (int i = start; i < end; i++) {
      buf.append(' ').append( hex[ (b[i] >> 4) & 0x0f ])
                     .append( hex[ (b[i]     ) & 0x0f ]);
    }
    return buf;
  }

  char[] hex = {'0','1','2','3','4','5','6','7',
                '8','9','a','b','c','d','e','f'};

}
