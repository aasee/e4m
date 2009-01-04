package e4m.tcp.script;

import java.net.Socket;

public class TcpClient {

  public static void main(String[] args) throws Exception {
    new TcpClient().start(args);
  }

  void start(String[] args) throws Exception {
    Socket peer = Tcp.getPeer(args[0],args[1]);
    new TcpTask(args[2]).service(peer);
    peer.close();
  }
  
}
