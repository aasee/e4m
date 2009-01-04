package e4m.net.telnet;

import static e4m.ref.Ascii.*;

public class TelnetCommands {

  private int[] remappedCommandCodes =

                new int[] { DC1,    // NOP                 = 241
                            DC2,    // DataMark            = 242
                            DC3,    // Break               = 243
                            DC4,    // InterruptProcess    = 244
                            FS,     // AbortOutput         = 245
                            GS,     // AreYouThere         = 246
                            US,     // EraseCharacter      = 247
                            RS,     // EraseLine           = 248
                            ENQ };  // GoAhead             = 249

  public void mapTerminalCommands( int NOP,
                                   int DataMark,
                                   int Break,
                                   int InterruptProcess,
                                   int AbortOutput,
                                   int AreYouThere,
                                   int EraseCharacter,
                                   int EraseLine,
                                   int GoAhead )
  {
    remappedCommandCodes = new int[] { NOP,               // DC1
                                       DataMark,          // DC2
                                       Break,             // DC3
                                       InterruptProcess,  // DC4
                                       AbortOutput,       // FS
                                       AreYouThere,       // GS
                                       EraseCharacter,    // US
                                       EraseLine,         // RS
                                       GoAhead };         // ENQ
  }

  public int getTerminalCommand(int b) {
    return (b < 241 || b > 249) ? -1 : remappedCommandCodes[ b - 241 ] & 0x0ff;
  }

}
