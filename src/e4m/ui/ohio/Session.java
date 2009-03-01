package e4m.ui.ohio;

import org.ohio.OhioScreen;
import org.ohio.OhioSession;
import static org.ohio.Ohio.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

class Session implements OhioSession {

  URLConnection connection;

  String configurationResource;
  String sessionName;

  Screen screen;

  Session(String configurationResource, String sessionName) {
    this.configurationResource = configurationResource;
    this.sessionName = sessionName;
  }

  public OhioScreen getScreen() throws IOException { return screen; }

  public String getConfigurationResource() { return configurationResource; }
  public String getSessionName() { return sessionName; }

  public Type getSessionType() {
    String p = connection.getURL().getProtocol();
    if (p.equalsIgnoreCase("tn3270")) return Type.TN3270;
    if (p.equalsIgnoreCase("tn5250")) return Type.TN5250;
    return Type.UNKNOWN;
  }

  public void connect() throws IOException {
    connection = new URL(configurationResource).openConnection();

    screen = new Screen(this);
    screen.setSize(0,0);
    screen.setCodec(null);

    // TODO: set configuration information
    //       Device size
    //       Charset
  }

  public void disconnect() throws IOException {
    connection.getInputStream().close();
    connection.getOutputStream().close();
    connection = null;
    screen = null;
  }

  public boolean isConnected() { return (connection != null); }

  Object refresh() throws IOException {
    return connection.getContent();
  }

  void update(byte[] data) throws IOException {
    connection.getOutputStream().write(data);
  }

}
