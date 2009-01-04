package e4m.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class Connection extends URLConnection {

  protected Connection(URL url, ProtocolHandler protocol) {
    super(url);
    this.protocol = protocol;
    this.doInput = true;
    this.doOutput = true;
  }

  protected ProtocolHandler protocol;
  protected InputStream in;
  protected OutputStream out;

  public int nextByte() throws IOException {
    return in.read();
  }

  public int readByte() throws IOException {
    int b = in.read();
    if (b < 0) throw new EOFException();
    return b;
  }

  public void readBytes(byte[] buf) throws IOException {
    for (int i = 0; i < buf.length; i++)
      buf[i] = (byte) readByte();
  }

  public void writeByte(int b) throws IOException {
    out.write(b);
  }

  public void writeBytes(int ... bytes) throws IOException {
    for (int b : bytes)
      out.write(b);
  }


  public void writeBytes(byte[] ... fragments) throws IOException {
    for (byte[] buf : fragments)
      out.write(buf);
  }


}
