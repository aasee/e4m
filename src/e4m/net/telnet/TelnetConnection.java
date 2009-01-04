package e4m.net.telnet;

import e4m.net.Connection;
import e4m.net.ProtocolHandler;
import e4m.ref.STD_0008;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TelnetConnection extends Connection {

  protected Socket socket;

  protected InputStream divert;

  protected OutputStream filter =
    new OutputStream() {
      @Override
      public void close() throws IOException { out.close(); }

      @Override
      public void flush() throws IOException { out.flush(); }

      @Override
      public void write(int b) throws IOException {
        if (b == STD_0008.IAC) out.write(STD_0008.IAC);
        out.write(b);
      }
    };

  protected TelnetOptions options;
  protected TelnetCommands commands;

  protected TelnetConnection(URL url, ProtocolHandler protocol ) {
    super(url,protocol);

    commands = new TelnetCommands();
    options = new TelnetOptions();
  }

  @Override
  public void connect() throws IOException {
    if (connected) return;

    socket = new Socket(url.getHost(),
                        url.getPort() > 0 ? url.getPort() : url.getDefaultPort());

    in = socket.getInputStream();
    out = socket.getOutputStream();
    divert = protocol.divert(this);

    connected = true;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    connect();
    return divert;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    connect();
    return filter;
  }

  public InputStream getDirectInputStream() { return in; }
  public OutputStream getDirectOutputStream() { return out; }

  @Override
  public String getHeaderField(String name) {
    boolean state = connected;
    connected = false;
    String s = getRequestProperty(name);
    connected = state;
    return s;
  }

  public void setHeaderField(String key, String value) {
    boolean state = connected;
    connected = false;
    setRequestProperty(key,value);
    connected = state;
  }

  @Override
  public Map<String,List<String>> getHeaderFields() {
    boolean state = connected;
    connected = false;
    Map<String,List<String>> m = getRequestProperties();
    connected = state;
    return m;
  }

  /***
   *   unsupported API
   *
   *   String getHeaderField(int n)
   *   String getHeaderFieldKey(int n)
   *
   *   String getContentEncoding() -> 'content-encoding'
   *      int getContentLength()   -> 'content-length'
   *   String getContentType()     -> 'content-type'
   *     long getDate()            -> 'date'
   *     long getExpiration()      -> 'expires'
   *     long getLastModified()    -> 'last-modified'
   */

}
