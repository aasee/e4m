package e4m.io;

import java.io.IOException;
import java.io.InputStream;

public class ByteEmitter extends InputStream {

  byte[] buf;
  int position, limit;

  public ByteEmitter(byte[] b, int off, int len) {
    this.buf = b;
    this.position = off;
    this.limit = off + len;
  }

  @Override
  public int available() throws IOException {
    return (limit - position);
  }

  @Override
  public void close() throws IOException {
    position = limit;
  }

  @Override
  public int read() throws IOException {
    return (position < limit) ? buf[position++] : -1;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return read(b,0,b.length);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int i = off;
    while (i < off+len) {
      int r = read();
      if (r < 0) break;
      b[i++] = (byte) r;
    }
    return i - off;
  }

  @Override
  public long skip(long n) throws IOException {
    int s = (int)n;
    if (s > available()) s = available();
    position += s;
    return (long) s;
  }

  @Override
  public boolean markSupported() { return false; }

  @Override
  public synchronized void mark(int readLimit) {}

  @Override
  public synchronized void reset() throws IOException {}

}
