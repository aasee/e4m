package e4m.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLStreamHandler;

public abstract class ProtocolHandler extends URLStreamHandler {

  // singleton instantiated in e4m.net.{protocol}.Handler

  final static int IAC = 255;

  public InputStream divert(final Connection connection) {

    return new InputStream() {
      Connection c = connection;
      boolean EOB = false;

      @Override
      public synchronized void mark(int readlimit) {
        EOB = true;
      }

      @Override
      public synchronized void reset() {
        EOB = false;
      }

      @Override
      public int read() throws IOException {
        int b;
        for (;;) {
          if (EOB) return -1;
          b = c.nextByte();
          if (b != IAC) return b;
          b = c.readByte();
          if (b == IAC) return b;
          b = handleControlSequence(c,b);
          if (b != IAC) return b;
        }
      }
    };
  }

  abstract
  protected int handleControlSequence(Connection c, int cmd) throws IOException;

}
