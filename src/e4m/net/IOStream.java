package e4m.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface IOStream {

  InputStream in();
  OutputStream out();
  Socket socket();
  
  void close() throws IOException;
  void flush() throws IOException;
  
  int read(byte[] buf) throws IOException;
  int read(byte[] buf, int off, int len) throws IOException;

  void write(byte[] buf) throws IOException;
  void write(byte[] buf, int off, int len) throws IOException;
  
}
