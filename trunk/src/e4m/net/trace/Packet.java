package e4m.net.trace;

public interface Packet {
  
  String timestamp();
  String source();
  String destination();
  String flags();
  byte[] data();

}
