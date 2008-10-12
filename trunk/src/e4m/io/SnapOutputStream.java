package e4m.io;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

/*
   0----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8
     000000  00 01 02 03 04 05 06 07  08 09 0a 0b 0c 0d 0e 0f  |................|
*/

public class SnapOutputStream extends OutputStream {

  BufferedWriter out;
  String encoding;
  
  byte[] buf = new byte[16];
  int count = 0;
  int offset = 0;

  public SnapOutputStream(Writer out) {
    this(out, new OutputStreamWriter(System.out).getEncoding());
  }

  public SnapOutputStream(Writer out, String encoding) {
    this.encoding = encoding;
    this.out = (out instanceof BufferedWriter) ? (BufferedWriter)out : new BufferedWriter(out);
  }

  public void write(int b) throws IOException {
    if (count >= buf.length)
      flush();
    buf[count++] = (byte)b;
  }

  public void close() throws IOException {
    flush();
  }

  public void flush() throws IOException {
    if (count < 1) return;
    String s;

    out.write(' ');
    out.write(' ');

    s = "000000"+Integer.toHexString(offset);
    out.write(s,s.length()-6,6);
    
    out.write(' ');
    out.write(' ');

    int i;
    for (i = 0; i < count; i++) {
      if (i == 8) out.write(' ');
      s = "00"+Integer.toHexString(buf[i]);
      out.write(s,s.length()-2,2);
      out.write(' ');
    }
    while (i++ < buf.length) {
      if (i == 9) out.write(' '); 
      out.write(' ');
      out.write(' ');
      out.write(' ');
    }
    out.write(' ');
    out.write('|');

    s = new String(buf,0,count,encoding); 
    for (i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      out.write((c < 0x20 || c > 0x7e) ? '.' : c);
    }
    while (i++ < buf.length) {
      out.write(' ');
    }
    out.write('|');
    
    out.newLine();
    out.flush();
    
    offset += count;
    count = 0;
  }
  
  public void reset() {
    count = offset = 0;
  }
  
  public void writeBytes(byte[] buf) throws IOException {
    write(buf);  
  }
  
  public void writeBytes(byte[] buf, int off, int len) throws IOException {
    write(buf,off,len);  
  }
  
}
