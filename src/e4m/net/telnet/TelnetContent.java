package e4m.net.telnet;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URLConnection;

public class TelnetContent extends ContentHandler {

  @Override
  public Object getContent(URLConnection urlc) throws IOException {
    return "Not supported yet.";
  }

}
