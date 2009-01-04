package e4m.tcp.trace;

public interface TcpdumpPacket {
  
  String timestamp();
  String source();
  String destination();
  String flags();
  byte[] data();
  int datalength();
}
