package e4m.ref;

public interface GA23_0059 {

  interface E { // EBCDIC

    // 3270 commands
  
    final static int Wr               = 0x0F1; // Write
    final static int EW               = 0x0F5; // Erase Write
    final static int EWA              = 0x07E; // Erase Write Alternate
  
    final static int RB               = 0x0F2; // Read Buffer
    final static int RM               = 0x0F6; // Read Modified
    final static int RMA              = 0x06E; // Read Modified Alternate
  
    final static int EAU              = 0x06F; // Erase All Unproredted
  
    final static int WSF              = 0x0F3; // Write Structured Field
  
    // 3270 orders
  
    final static int SF               = 0x01D; // Start Field
    final static int SFE              = 0x029; // Start Field Extended
    final static int SBA              = 0x011; // Set Buffer Address
    final static int SA               = 0x028; // Set Attributes
    final static int MF               = 0x02C; // Modify Field
    final static int IC               = 0x013; // Insert Cursor
    final static int PT               = 0x005; // Program Tab
    final static int RA               = 0x03C; // Repeat to Address
    final static int EUA              = 0x012; // Erase Unprotected to Address
    final static int GE               = 0x008; // Graphic Escape
  }

  interface A { // ASCII codes for commands and orders

    final static int Wr               = 0x031;
    final static int EW               = 0x035;
    final static int EWA              = 0x03D;

    final static int RB               = 0x032;
    final static int RM               = 0x036;
    final static int RMA              = 0x03E;

    final static int EAU              = 0x03F;

    final static int SF               = 0x01D;
    final static int SBA              = 0x011;
    final static int IC               = 0x013;
    final static int PT               = 0x009;
    final static int RA               = 0x014;
    final static int EUA              = 0x012;
  }

  interface S { // SCS command codes

    final static int Wr               = 0x001;
    final static int EW               = 0x005;
  }

  // start of 3270 data stream text range

  final static int TEXT               = 0x040;

  // 3270 WCC bits

  final static int RESET_PARTITION    = 0x040;
  final static int RESET_PRINTER      = 0x030;
  final static int START_PRINTER      = 0x008;
  final static int SOUND_ALARM        = 0x004;
  final static int RESTORE_KEYBOARD   = 0x002;
  final static int RESET_MDT          = 0x001;

  // 3270 Attribute types

  final static int AT_RESET           = 0x000; //   C  Reset all character attributes
  final static int AT_FIELD           = 0x0C0; // F    3270 Field attribute
  final static int AT_VALIDATION      = 0x0C1; // F    Field validation
  final static int AT_OUTLINING       = 0x0C2; // F    Field outlining
  final static int AT_HIGHLIGHTING    = 0x041; // F C  Extended highlighting
  final static int AT_FOREGROUND      = 0x042; // F C  Foreground color
  final static int AT_CHARACTER_SET   = 0x043; // F C  Character set
  final static int AT_BACKGROUND      = 0x045; // F C  Background color
  final static int AT_TRANSPARENCY    = 0x046; // F C  Transparency

  final static int AT_INPUT_CONTROL   = 0x0FE; // F    Input Control (DBCS only)

  // 3270 Field Attribute bits

  final static int FA_PROTECTED       = 0x020;
  final static int FA_NUMERIC         = 0x010;
  final static int FA_NONDISPLAY      = 0x00C;
  final static int FA_INTENSIFIED     = 0x008;
  final static int FA_MDT             = 0x001;

  // 3270 Validation attribute values
  
  final static int MANDATORY_FILL     = 0x004;
  final static int MANDATORY_ENTRY    = 0x002;
  final static int TRIGGER            = 0x001;

  // 3270 Color values

  final static int NO_COLOR           = 0x000;
  final static int NEUTRAL_BLACK      = 0x0F0;
  final static int BLUE               = 0x0F1;
  final static int RED                = 0x0F2;
  final static int PINK               = 0x0F3;
  final static int GREEN              = 0x0F4;
  final static int TURQUOISE          = 0x0F5;
  final static int YELLOW             = 0x0F6;
  final static int NEUTRAL_WHITE      = 0x0F7;
  final static int BLACK              = 0x0F8;
  final static int DEEP_BLUE          = 0x0F9;
  final static int ORANGE             = 0x0FA;
  final static int PURPLE             = 0x0FB;
  final static int PALE_GREEN         = 0x0FC;
  final static int PALE_TURQUOISE     = 0x0FD;
  final static int GREY               = 0x0FE;
  final static int WHITE              = 0x0FF;

  // 3270 Character Set ID ranges

  final static int DEFAULT_CHARACTER_SET           = 0x000;
  final static int LOADABLE_CHARACTER_SET_LOW      = 0x040;
  final static int LOADABLE_CHARACTER_SET_HIGH     = 0x0EF;
  final static int NON_LOADABLE_CHARACTER_SET_LOW  = 0x0F0;
  final static int NON_LOADABLE_CHARACTER_SET_HIGH = 0x0F7;
  final static int DBCS_CHARACTER_SET_LOW          = 0x0F8;
  final static int DBCS_CHARACTER_SET_HIGH         = 0x0FE;

  /* 
      loadable character ranges
   
      X'00' through X'3F' and X'FF' are control codes
      X'40' is a nonloadable space
      X'41' through X'FE' are loadable graphics
  */
  
  // 3270 Outlining attribute bit mask values

  final static int UNDER_LINE         = 0x001;
  final static int RIGHT_LINE         = 0x002;
  final static int OVER_LINE          = 0x004;
  final static int LEFT_LINE          = 0x008;
  
  // 3270 Extended Highlighting attribute values
  
  final static int DEFAULT_HIGHLIGHT  = 0x000;
  final static int NORMAL_HIGHLIGHT   = 0x0F0;
  final static int BLINK              = 0x0F1;
  final static int REVERSE_VIDEO      = 0x0F2;
  final static int UNDERSCORE         = 0x0F4;

  // 3270 Transparency attribute values
  
  final static int DEFAULT_BACKGROUND = 0x000;
  final static int TRANSPARENT_OR     = 0x0F0;
  final static int TRANSPARENT_XOR    = 0x0F1;
  final static int OPAQUE             = 0x0FF;

  // 3270 DBCS Input Control attribute values
  
  final static int SOSI_DISABLE       = 0x000;
  final static int SOSI_ENABLE        = 0x001;

  // 3270 structured field ID's, etc.

  final static int READ_PARTITION     = 0x001;

  final static int QUERY_OPERATIONS   = 0x0FF;
  final static int QUERY              = 0x002;

  final static int QUERY_REPLY        = 0x088;
  
}
