package e4m.ref;

public interface STD_0008 {

  /* RFC 885 - End Of Record */

  static final int  END_OF_RECORD      =   25;  // option
  static final int  EOR                =  239;  // command

  /* RFC 1091 - Terminal Type option */

  static final int  TERMINAL_TYPE      =   24;

  /* Terminal-type verb codes */

  static final int  TERMINAL_IS        =    0;  // prefixed with TERMINAL_ to
  static final int  TERMINAL_SEND      =    1;  // avoid conflict with rfc2355 usage

  /* RFC 858 - Suppress Go Ahead option */

  static final int  SUPPRESS_GO_AHEAD  =    3;

  /* RFC 857 - Echo option */

  static final int  ECHO               =    1;

  /* RFC 856 - Binary option */

  static final int  TRANSMIT_BINARY    =    0;

  /* RFC 854 - Telnet Commands */

  static final int  SE                 =  240;
  static final int  NOP                =  241;
  static final int  DATA_MARK          =  242;
  static final int  BREAK              =  243;
  static final int  INTERRUPT_PROCESS  =  244;
  static final int  ABORT_OUTPUT       =  245;
  static final int  ARE_YOU_THERE      =  246;
  static final int  ERASE_CHARACTER    =  247;
  static final int  ERASE_LINE         =  248;
  static final int  GO_AHEAD           =  249;
  static final int  SB                 =  250;
  static final int  WILL               =  251;
  static final int  WONT               =  252;
  static final int  DO                 =  253;
  static final int  DONT               =  254;
  static final int  IAC                =  255;

}
