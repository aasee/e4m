package e4m.net.trace;

class TcpdumpRecord {

  int ip_version;
  int ip_header_length;
  int type_of_service;
  int total_length;
  int identification;
  int ip_flags;
  int fragment_offset;
  int time_to_live;
  int protocol;
  int ip_header_checksum;
  int source_address;
  int destination_address;

  byte[] ip_options;

  int source_port;
  int destination_port;
  int sequence_number;
  int acknowledgement_number;
  int tcp_header_length;
  int tcp_flags;
  int window;
  int tcp_checksum;
  int urgent_pointer;

  byte[] tcp_options;

  byte[] data;

  String timestamp;
  String source;
  String destination;
  String flags;

  // IP Version

  final static int IPv4 = 0x040;
  final static int IPv6 = 0x060;

  // IP Protocol ID values

  final static int ICMP = 1;
  final static int TCP  = 6;
  final static int UDP  = 17;

  // IP Flag bits

  final static int DO_NOT_FRAGMENT       = 0x04000;
  final static int MORE_FRAGMENTS_FOLLOW = 0x02000;

  // TCP Flag bits

  final static int CWR    = 0x080;
  final static int ECE    = 0x040;
  final static int URGENT = 0x020;
  final static int ACK    = 0x010;
  final static int PUSH   = 0x008;
  final static int RESET  = 0x004;
  final static int SYN    = 0x002;
  final static int FIN    = 0x001;

}

/*
   http://www.faqs.org/docs/gazette/tcp.html

   The TCP and IP packet format


       ASCII representation from RFC 791

    0                   1                   2                   3   
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1       
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  
   |Version|  IHL  |Type of Service|          Total Length         |  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  
   |         Identification        |Flags|      Fragment Offset    |  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  
   |  Time to Live |    Protocol   |         Header Checksum       |  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  
   |                       Source Address                          |  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  
   |                    Destination Address                        |  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  
   |                    Options                    |    Padding    |  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  


       ASCII representation from RFC 793

    0                   1                   2                   3     
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1  
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+      
   |          Source Port          |       Destination Port        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Sequence Number                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Acknowledgment Number                      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Data |           |U|A|P|R|S|F|                               |
   | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
   |       |           |G|K|H|T|N|N|                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |           Checksum            |         Urgent Pointer        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Options                    |    Padding    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                             data                              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

*/
