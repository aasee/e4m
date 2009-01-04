package e4m.net.tn3270;

import e4m.net.RequestProperties;
import e4m.net.telnet.TelnetConnection;
import e4m.net.telnet.TelnetOptions;
import e4m.net.telnet.TelnetProtocol;
import static e4m.ref.STD_0008.*;
import static e4m.ref.RFC_2355.*;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

/*
    tn3270 : // userid @ hostname : port / session ? MODEL=IBM3279-2-E
                                                   & LUNAME=DEV001
                                                   & LUPOOL=GRP02
                                                   & CODEPAGE=Cp037,Cp500

    MODEL    -> "Device-Type"
    LUNAME   -> "Device-Name"
    LUPOOL   -> "Resource-Name"
    CODEPAGE -> "Content-Encoding"
*/

public class Tn3270Protocol extends TelnetProtocol {

  @Override
  protected URLConnection openConnection(URL u) throws IOException {
    Tn3270Connection c = new Tn3270Connection(u,this);

    RequestProperties.set(c, "Content-Type",     "tn3270/datastream",
                             "Content-Encoding", "Cp037",
                             "Device-Type",      "IBM-3278-2" );
    
    RequestProperties.mapQuery(c, "Device-Type",      "MODEL",
                                  "Device-Name",      "LUNAME",
                                  "Resource-Name",    "LUPOOL",
                                  "Content-Encoding", "CODEPAGE" );

    TelnetOptions.set(c, ECHO,
                         TRANSMIT_BINARY, TERMINAL_TYPE, END_OF_RECORD,
                         TN3270E );

    return c;
  }

  @Override
  protected int InterpretAsCommand(TelnetConnection c, int cmd) throws IOException {
    switch (cmd) {
      default: return super.InterpretAsCommand(c,cmd);

      case EOR:  c.getInputStream().mark(0);  // temporarily block the input stream
                 return IAC;
    }
  }

  @Override
  protected void TelnetSubOption(TelnetConnection c, int opt) throws IOException {
    switch(opt) {
      default: super.TelnetSubOption(c,opt);

      case TN3270E:        Tn3270E((Tn3270Connection)c);      break;
      case TERMINAL_TYPE:  TerminalType((Tn3270Connection)c); break;
    }
  }

  // server -> IAC SB TERMINAL-TYPE SEND IAC SE
  // client -> IAC SB TERMINAL-TYPE IS ... IAC SE

  void TerminalType(Tn3270Connection c) throws IOException {
    int b = c.readByte();
    switch (b) {
      default: throw new IllegalArgumentException(
                           "unkown TERMINAL-TYPE sub option: "+toHex(b));

      case TERMINAL_SEND:  TerminalType_Send(c); break;
    }
  }

  void TerminalType_Send(Tn3270Connection c) throws IOException {
    c.writeBytes( new byte[] { (byte) IAC,
                               (byte) SB,
                               (byte) TERMINAL_TYPE,
                               (byte) TERMINAL_IS  },

                  c.getHeaderField("Terminal-Type").getBytes(),

                  new byte[] { (byte) IAC,
                               (byte) SE  }  );
  }

  // server-> IAC SB TN3270E ... IAC SE

  // server-> IAC SB TN3270E SEND DEVICE-TYPE IAC SE
  // client-> IAC SB TN3270E DEVICE-TYPE REQUEST <device-type> [ [CONNECT <resource-name>] | [ASSOCIATE <device-name>] ] IAC SE

  // server-> IAC SB TN3270E DEVICE-TYPE IS <device-type> CONNECT <device-name> IAC SE
  // server-> IAC SB TN3270E DEVICE-TYPE REJECT REASON <reason-code> IAC SE

  void Tn3270E(Tn3270Connection c) throws IOException {
    int b = c.readByte();
    switch (b) {
      default: throw new ProtocolException(
                           "unknown TN3270E option: "+toHex(b));

      case SEND:         Tn3270E_Send(c);       break;
      case DEVICE_TYPE:  Tn3270E_DeviceType(c); break;
      case FUNCTIONS:    Tn3270E_Functions(c);  break;
    }
  }

  void Tn3270E_DeviceType(Tn3270Connection c) throws IOException {
    int b = c.readByte();
    switch (b) {
      default: throw new ProtocolException(
                           "unknown TN3270E DEVICE-TYPE verb: "+toHex(b));

      case IS:      Tn3270E_DeviceType_Is(c);
                    Server_Functions_Request(c);
                    break;

      case REJECT:  Tn3270E_DeviceType_Reject(c);
                    break;
    }
  }

  void Tn3270E_Send(Tn3270Connection c) throws IOException {
    int b = c.readByte();
    switch (b) {
      default: throw new ProtocolException(
                           "unknown TN3270E SEND option: "+toHex(b));

      case DEVICE_TYPE:  Tn3270E_DeviceType_Request(c); break;
    }
    SubOption_End(c);
  }

  void Tn3270E_DeviceType_Request(Tn3270Connection c) throws IOException {
    String resourceName = c.getHeaderField("Resource-Name");
    String deviceName   = c.getHeaderField("Device-Name");
    String deviceType   = c.getHeaderField("Device-Type");

    byte[] prefix = { (byte) IAC,
                      (byte) SB,
                      (byte) TN3270E,
                      (byte) DEVICE_TYPE,
                      (byte) REQUEST };

    byte[] device = deviceType.getBytes();

    byte[] suffix = { (byte) IAC,
                      (byte) SE  };

    if (deviceName != null && deviceName.length() > 0) {
      c.writeBytes( prefix,
                    device,
                    new byte[] { (byte) ASSOCIATE },
                    deviceName.getBytes(),
                    suffix );
    }
    else
    if (resourceName != null && resourceName.length() > 0) {
      c.writeBytes( prefix,
                    device,
                    new byte[] { (byte) CONNECT },
                    resourceName.getBytes(),
                    suffix );
    }
    else {
      c.writeBytes( prefix, device, suffix );
    }
  }

  void Tn3270E_DeviceType_Is(Tn3270Connection c) throws IOException {
    StringBuilder dev = readSubOptionText(c);
    int pos = indexOf( dev, CONNECT );
    if (pos < 0) {
      throw new ProtocolException(
                  "incomplete TN3270E DEVICE-TYPE description: " + dev);
    }
    c.setHeaderField("Device-Type",dev.substring(0,pos));
    c.setHeaderField("Device-Name",dev.substring(pos+1));
  }

  void Tn3270E_DeviceType_Reject(Tn3270Connection c) throws IOException {
    int r = c.readByte();
    switch (r) {
      default: throw new ProtocolException(
            "Tn3270E DEVICE-TYPE " + c.getHeaderField("Device-Type")
                   + " rejected: " + toHex(r));

      case REASON: throw new ProtocolException(
            "TN3270E DEVICE-TYPE " + c.getHeaderField("Device-Type")
            + " rejected: reason " + REASON(c.readByte()) );
    }
    // SubOption_End(c);
  }

  // both-> IAC SB TN3270E FUNCTIONS REQUEST <function-list> IAC SE
  // both-> IAC SB TN3270E FUNCTIONS IS <function-list> IAC SE

  void Tn3270E_Functions(Tn3270Connection c) throws IOException {
    int b = c.readByte();
    switch (b) {
      default: throw new ProtocolException(
                           "unknown TN3270E FUNCTIONS verb: " + toHex(b));

      case REQUEST: Tn3270E_Functions_Request(c);
                    // Server_Functions_Request(c);
                    break;

      case IS:      Tn3270E_Functions_Is(c);
                    break;
    }
  }

  void Tn3270E_Functions_Request(Tn3270Connection c) throws IOException {
    c.writeBytes( IAC, SB,
                  TN3270E, FUNCTIONS, IS,
                  BIND_IMAGE, DATA_STREAM_CTL, RESPONSES,
                  // SCS-CTL-CODES, SYSREQ,
                  IAC, SE );
  }

  void Server_Functions_Request(Tn3270Connection c) throws IOException {
    c.writeBytes( IAC, SB,
                  TN3270E, FUNCTIONS, REQUEST,
                  BIND_IMAGE, RESPONSES, SYSREQ,
                  IAC, SE );
  }

  void Tn3270E_Functions_Is(Tn3270Connection c) throws IOException {
    c.serverFunction(-1);
    StringBuilder dev = readSubOptionText(c);
    for (int i = 0; i < dev.length(); i++)
      c.serverFunction(dev.charAt(i));
  }

  int indexOf(CharSequence seq, int ch) {
    for (int i = 0; i < seq.length(); i++) {
      if (seq.charAt(i) == (char)ch)
        return i;
    }
    return -1;
  }

  StringBuilder readSubOptionText(Tn3270Connection c) throws IOException {
    int b = 0;
    StringBuilder buf = new StringBuilder();
    for (;;) {
      b = c.nextByte();
      if (b == IAC) {
        b = c.nextByte();
        if (b == SE) break;
        if (b != IAC) throw new ProtocolException("unexpected IAC/"+toHex(b)+" sequence");
      }
      if (b < 0) throw new ProtocolException("broken IAC/SB sequence");
      buf.append((char)b);
    }
    return buf;
  }

  static String REASON(int b) {
    switch (b) {
      default: return toHex(b);

      case 0: return "CONN-PARTNER";
      case 1: return "DEVICE-IN-USE";
      case 2: return "INV-ASSOCIATE";
      case 3: return "INV-NAME";
      case 4: return "INV-DEVICE-TYPE";
      case 5: return "TYPE-NAME-ERROR";
      case 6: return "UNKNOWN-ERROR";
      case 7: return "UNSUPPORTED-REQ";
    }
  }

}
