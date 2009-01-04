package e4m.ref;

public interface AID {                    // EBCDIC   graphic  ASCII

  static final int  NO_AID                  = 0x060;  //       2D
  static final int  NO_AID_PRINTER          = 0x0E8;  //   Y   59

  static final int  STRUCTURED_FIELD        = 0x088;  //   h
  static final int  READ_PARTITION          = 0x061;  //   /

  static final int  TRIGGER                 = 0x07F;  //   "

  static final int  SYS_REQ                 = 0x0F0;  //   0   30

  static final int  PF1                     = 0x0F1;  //   1   31
  static final int  PF2                     = 0x0F2;  //   2   32
  static final int  PF3                     = 0x0F3;  //   3   33
  static final int  PF4                     = 0x0F4;  //   4   34
  static final int  PF5                     = 0x0F5;  //   5   35
  static final int  PF6                     = 0x0F6;  //   6   36
  static final int  PF7                     = 0x0F7;  //   7   37
  static final int  PF8                     = 0x0F8;  //   8   38
  static final int  PF9                     = 0x0F9;  //   9   39
  static final int  PF10                    = 0x07A;  //   :   3A
  static final int  PF11                    = 0x07B;  //   #   23
  static final int  PF12                    = 0x07C;  //   @   40
  static final int  PF13                    = 0x0C1;  //   A   41
  static final int  PF14                    = 0x0C2;  //   B   42
  static final int  PF15                    = 0x0C3;  //   C   43
  static final int  PF16                    = 0x0C4;  //   D   44
  static final int  PF17                    = 0x0C5;  //   E   45
  static final int  PF18                    = 0x0C6;  //   F   46
  static final int  PF19                    = 0x0C7;  //   G   47
  static final int  PF20                    = 0x0C8;  //   H   48
  static final int  PF21                    = 0x0C9;  //   I   49
  static final int  PF22                    = 0x04A;  //   c   5B
  static final int  PF23                    = 0x04B;  //   .   2E
  static final int  PF24                    = 0x04C;  //   <   3C

  static final int  PA1                     = 0x06C;  //   %   25
  static final int  PA2                     = 0x06E;  //   >   3E
  static final int  PA3                     = 0x06B;  //   ,   2C

  static final int  CLEAR                   = 0x06D;  //   -   5F
  static final int  CLEAR_PARTITION         = 0x06A;  //

  static final int  ENTER                   = 0x07D;  //   ,   27

  static final int  SELECTOR_PEN            = 0x07E;  //   =   3D

  static final int  OPERATOR_ID_READER      = 0x06E;  //   W   57
  static final int  MAGNETIC_READER_NUMBER  = 0x0E7;  //   X   58

  static final int  INTERRUPT_PROCESS       = -1;     //   Telnet Interrupt Process 
}
