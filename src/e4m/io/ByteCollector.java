package e4m.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteCollector extends OutputStream {

  byte[] buf;
  int size, count;

  public ByteCollector() {
    this(64);
  }

  public ByteCollector(int size) {
    this.size = size;
    close();
  }

  @Override
  public void close() {
    flush();
    buf = new byte[size];
  }

  @Override
  public void flush() {
    count = 0;
  }

  @Override
  public void write(int b) throws IOException {
    if (count >= buf.length) {
      buf = Arrays.copyOf(buf, buf.length + size);
    }
    buf[count++] = (byte) b;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    for (int i = off; i < off+len; i++) write(b[i]);
  }

  public ByteBuffer toByteBuffer() {
    return ByteBuffer.wrap(buf,0,count);
  }

  public InputStream toInputStream() {
    return new ByteEmitter(buf,0,count);
  }

}
