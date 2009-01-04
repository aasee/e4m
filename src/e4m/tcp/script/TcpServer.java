package e4m.tcp.script;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TcpServer {
  
  public static void main(String[] args) throws Exception {
    new TcpServer().start(args);
  }
  
  void start(String[] args) throws Exception {
    
    active = Collections.synchronizedSet(new HashSet<Socket>());
    
    ServerSocket server = Tcp.getServer(args[0],args[1]);
    TcpTask service = new TcpTask(args[2]);
    
    while (server.isBound()) {
      Socket client = Tcp.getClient(server);
      runTask(service,client);
    }

    server.close();
    
    for (Socket client : active) {
      client.close();
    }  
  }

  Set<Socket> active;
  

  public void runTask(TcpTask task, Socket client) {
    Thread thread = new Thread(new Servlet(task,client));
    thread.start();
  }

  class Servlet implements Runnable {

    Servlet(TcpTask task, Socket client) {
      this.task = task;
      this.client = client;
    }

    TcpTask task;
    Socket client;

    public void run() {
      try {
        String id = "Thread[id="+Thread.currentThread().getId()+"] "+client;
        System.err.println("start: "+id);
        active.add(client);
        task.service(client);
        client.close();
        active.remove(client);
        System.err.println("stop: "+id);
      }
      catch (Exception e) {
        RuntimeException re = new RuntimeException(e);
        re.setStackTrace(e.getStackTrace());
        throw re;
      }
    }
  }

}
