package e4m.net.trace;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
     \15:43:59.148363 IP 128.96.130.112.49579 > 128.96.184.2.23: S 1730357317:1730357317(0) win 24820 <nop,nop,sackOK,mss 1460>
     \t0x0000:  4500 0030 382a 4000 4006 c76a 8060 8270  E..08*@.@..j.`.p  
*/ 

public class TcpdumpReader extends BufferedReader {

  public TcpdumpReader(Reader in) {
    super(in);
  }

  public String readLine() throws IOException {
    line = "";
    index = 50;
    return super.readLine();
  }
  
  //  0----+----1----+----2----+----3----+----4----+----5----+----6----+----7
  //  ^0x0000:  0000 1111 2222 3333 4444 5555 6666 7777  0123456789abcdef
  
  String line;
  int index;
  
  public int readByte() throws IOException {
    if (index > 48) readTrace();
    char a = line.charAt(index++);
    if (a == ' ') a = line.charAt(index++);
    if (a == ' ') throw new EOFException();
    char b = line.charAt(index++);
    return ( hexa(a) << 4 ) | hexa(b);
  }
  
  public byte[] readBytes(int length) throws IOException {
    byte[] buf = new byte[length];
    for (int i = 0; i < buf.length; i++)
      buf[i] = (byte) readByte();
    return buf;
  }
  
  public int readShort() throws IOException {
    return ( readByte() << 8 )
         | ( readByte()      );
    
  }
  
  public int readInt() throws IOException {
    return ( readByte() << 24 )
         | ( readByte() << 16 )
         | ( readByte() <<  8 )
         | ( readByte()       );
  }
  
  int hexa(char c) throws IOException {
    if (c >= '0' && c <= '9') return (c - '0');
    if (c >= 'A' && c <= 'F') return (c - 'A') + 10;
    if (c >= 'a' && c <= 'f') return (c - 'a') + 10;
    throw new IOException("invalid character '"+c+"'"); 
  }
  
  void readTrace() throws IOException {
    line = readLine();
    if (line == null) throw new EOFException();
    index = 10;
    if (line.startsWith("\t0x")) return;
    throw new IOException("no hexadecimal data");
  }  
  
  public Packet readPacket() throws IOException {
    String hdr = readLine();
    if (hdr == null) return null;
    
    TcpdumpRecord r = new TcpdumpRecord();
    readHeader(r,hdr);
    readIpHeader(r);
    readTcpHeader(r);
    readData(r);
    
    return new TcpdumpPacket(r);
  }

  //  hh:mm:ss.tttttt IP h.h.h.h.pp > h.h.h.h.pp: S b:e(l) ...
  
  Matcher desc =  // timestamp  source destination  flags  etc.
    Pattern.compile("([^ ]+) IP ([^ ]+) > ([^:]+): ([^ ]+) (.*)")
           .matcher("");
  
  void readHeader(TcpdumpRecord r, String description) {
    if (desc.reset(description).matches()) {
      r.timestamp = desc.group(1);
      r.source = desc.group(2);
      r.destination = desc.group(3);
      r.flags = desc.group(4);
    }
  }

  void readIpHeader(TcpdumpRecord r) throws IOException {
    int i;
                        i = readByte();
    r.ip_version          = ( i & 0x0f0 );
    r.ip_header_length    = ( i & 0x00f ) * 4;
    r.type_of_service     = readByte();
    r.total_length        = readShort();
    r.identification      = readShort();
                        i = readShort();
    r.ip_flags            = ( i & 0x0e000);
    r.fragment_offset     = ( i & 0x01fff);
    r.time_to_live        = readByte();
    r.protocol            = readByte();
    r.ip_header_checksum  = readShort();
    r.source_address      = readInt();
    r.destination_address = readInt();

    if (r.ip_header_length > 20) {
      r.ip_options = readBytes( r.ip_header_length - 20);
    }
  }

  void readTcpHeader(TcpdumpRecord r) throws IOException {

    r.source_port            = readShort();
    r.destination_port       = readShort();
    r.sequence_number        = readInt();
    r.acknowledgement_number = readInt();
    r.tcp_header_length   = (( readByte() & 0x0f0 ) >> 4 ) * 4;
    r.tcp_flags              = readByte();
    r.window                 = readShort();
    r.tcp_checksum           = readShort();
    r.urgent_pointer         = readShort();

    if (r.tcp_header_length > 20) {
      r.tcp_options = readBytes( r.tcp_header_length - 20 );
    }
  }
  
  void readData(TcpdumpRecord r) throws IOException {
    int header_length = r.ip_header_length
                      + r.tcp_header_length;
    if (r.total_length > header_length) {
      r.data = readBytes( r.total_length - header_length );
    }
  }
  
  class TcpdumpPacket implements Packet {
    TcpdumpPacket(TcpdumpRecord r) { this.r = r; }
    TcpdumpRecord r;

    public String timestamp() { return r.timestamp; }
    public String source() { return r.source; }
    public String destination() { return r.destination; }
    public String flags() { return r.flags; }
    public byte[] data() { return r.data; }
  }

}