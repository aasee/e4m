package e4m.js.trace;

public interface TcpdumpPacket {
  
  String timestamp();
  String source();
  String destination();
  String flags();
  byte[] data();
  int datalength();
}
