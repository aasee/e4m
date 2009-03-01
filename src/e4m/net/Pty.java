package e4m.net;

import java.io.IOException;
import java.net.URLConnection;

public interface Pty {

    void connect(String url) throws IOException;
    void disconnect() throws IOException;

    boolean connected();
    URLConnection connection();

    void configure(Vty vty);

    void poll(Vty vty) throws IOException;
    
}
