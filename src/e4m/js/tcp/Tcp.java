package e4m.js.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Tcp {

  public static SocketAddress getSocketAddress(String hostname, String port) throws UnknownHostException {
    return new InetSocketAddress(
        InetAddress.getByName(hostname), portByNumber(port));
  }

  public static int portByNumber(String portnumber) {
    int port = Integer.parseInt(portnumber);
    if (port < 0 || port > 65535) {
      throw new IllegalArgumentException(portnumber);
    }
    return port;
  }  
  
  public static ServerSocket getServer(String hostname, String port) throws IOException {
    SocketAddress sa = getSocketAddress(hostname,port);
    ServerSocket server = new ServerSocket();
    server.bind(sa,1);
    return server;
  }
  
  public static Socket getClient(ServerSocket server) throws IOException {
    Socket client = server.accept();
    client.setReceiveBufferSize(4096);
    client.setTcpNoDelay(true);
    return client;
  }
  
  public static Socket getPeer(String hostname, String port) throws IOException {
    Socket peer = new Socket();
    SocketAddress sa = getSocketAddress(hostname,port);
    peer.connect(sa);
    return peer;
  }
  
  public static IOStream connect(String host, String port) throws IOException {
    Socket peer = getPeer(host,port);
    return new IOS(peer);
  }
  
  public static IOStream listen(String host, String port) throws IOException {
    ServerSocket server = getServer(host,port);
    Socket client = getClient(server);
    return new IOS(client);
  }
  
  public static byte[] alloc(int len) {
    return new byte[len];
  }
  
  public static boolean cmp(byte[] a, byte[] b) {
    return Arrays.equals(a,b);
  }
  
  static class IOS implements IOStream {
    IOS(Socket socket) throws IOException {
      this.socket = socket;
      this.in = socket.getInputStream();
      this.out = socket.getOutputStream();
    }

    Socket socket;
    InputStream in;
    OutputStream out;
    
    public InputStream in() { return this.in; }
    public OutputStream out() { return this.out; }
    public Socket socket() { return this.socket; }
    
    public void close() throws IOException { socket.close(); }
    public void flush() throws IOException { out.flush(); }
    
    public int readFully(byte[] buf) throws IOException {
      int i = 0;
      while (i < buf.length) {
        int b = in.read();
        if (b < 0) break;
        buf[i++] = (byte) b;
      }
      return i;
    }

    public int read(byte[] buf) throws IOException {
      return in.read(buf);
    }
    public int read(byte[] buf, int off, int len) throws IOException {
      return in.read(buf,off,len);
    }
    public void write(byte[] buf) throws IOException {
      out.write(buf);
    }
    public void write(byte[] buf, int off, int len) throws IOException {
      out.write(buf,off,len);
    }
  }
  
}
