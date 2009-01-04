package e4m.net.telnet;

import e4m.net.Connection;
import e4m.net.ProtocolHandler;

import e4m.net.RequestProperties;
import static e4m.ref.STD_0008.*;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public abstract class TelnetProtocol extends ProtocolHandler {

  @Override
  protected URLConnection openConnection(URL u) throws IOException {
    TelnetConnection c = new TelnetConnection(u,this);

    RequestProperties.set(c, "Content-Type", "telnet/datastream" );

    return c;
  }  

  @Override
  protected int getDefaultPort() {
    return 23;
  }

  // protected static final int READ_NEXT_BYTE = 0x0100;

  @Override
  protected int handleControlSequence(Connection c, int cmd) throws IOException {
    return InterpretAsCommand((TelnetConnection)c,cmd);
  }
  
  protected int InterpretAsCommand(TelnetConnection c, int cmd) throws IOException {
    switch (cmd) {
      default:
        throw new ProtocolException("unsupported command: "+toHex(cmd));

      case NOP:
      case DATA_MARK:
      case BREAK:
      case INTERRUPT_PROCESS:
      case ABORT_OUTPUT:
      case ARE_YOU_THERE:
      case ERASE_CHARACTER:
      case ERASE_LINE:
      case GO_AHEAD:
        return TerminalAction(c,cmd);

      case WILL:              
      case WONT:              
      case DO:                
      case DONT:
        TelnetOption(c,cmd);
        return IAC;

      case SB:
        int opt = c.readByte();
        TelnetSubOption(c,opt);
        // ends with IAC,SE
        return IAC;
    }
  }

  protected void TelnetOption(TelnetConnection c, int cmd) throws IOException {
    int opt = c.readByte();
    switch(cmd) {
      default:
        throw new ProtocolException("unsupported command: "+toHex(cmd));

      case WILL:
        c.writeBytes( IAC, c.options.iWill(opt) ? DO : DONT, opt);
        break;
        
      case WONT:
        c.options.iCanNot(opt); c.writeBytes( IAC, DONT, opt );
        break;
        
      case DO: 
        c.writeBytes( IAC, c.options.iDo(opt) ? WILL : WONT, opt);
        break;
        
      case DONT: 
        c.options.iCanNot(opt); c.writeBytes( IAC, WONT, opt );
        break;        
    }
  }

  protected void TelnetSubOption(TelnetConnection c, int opt) throws IOException {
    for (;;) {
      System.out.println("sub");
      if (c.readByte() != IAC) continue;
      if (c.readByte() == SE) break;
    }
  }

  protected void SubOption_End(TelnetConnection c) throws IOException {
    if (c.readByte() == IAC && c.readByte() == SE) return;
    throw new ProtocolException("missing IAC/SE sequence");
  }

  protected int TerminalAction(TelnetConnection c, int cmd) throws IOException {
    return c.commands.getTerminalCommand(cmd);
  }

  protected static String toHex(int b) {
    return "<"+Integer.toHexString(b+256).substring(1)+'>';
  }

}
