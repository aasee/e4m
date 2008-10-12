package e4m.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

public final class IO {

  public static void writeBytes(OutputStream out, byte ... bytes) {
    writeBytes(out,bytes);
  }
  
  public static void writeBytes(OutputStream out, byte[] ... list) throws IOException {
    for (byte[] buf : list) for (byte b : buf) out.write(b);
  }

  public static int readByte(InputStream in) throws IOException {
    int b = in.read();
    if (b < 0) throw new InterruptedIOException("unexpected EOF");
    return b;
  }

  public static void readBytes(InputStream in, byte[] b, int off, int len) throws IOException {
    for (int i = off; i < off+len; i++) b[i] = (byte) readByte(in);
  }

}
