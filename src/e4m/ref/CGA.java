package e4m.ref;

// OHIO_PLANE_COLOR, // Color Plane (standard HLLAPI CGA color values)  

public interface CGA {

  static final int BACKGROUND     =  0; // x'F0'  -> ( BLACK on Displays,
  static final int BLUE           =  1; // x'F1'       WHITE on Printers )
  static final int RED            =  2; // x'F2'
  static final int PINK           =  3; // x'F3'
  static final int GREEN          =  4; // x'F4'
  static final int TURQUOISE      =  5; // x'F5'
  static final int YELLOW         =  6; // x'F6'
  static final int FOREGROUND     =  7; // x'F7'  -> ( WHITE on Displays,
  static final int BLACK          =  8; // x'F8'       BLACK on Printers )
  static final int DEEP_BLUE      =  9; // x'F9'
  static final int ORANGE         = 10; // x'FA'
  static final int PURPLE         = 11; // x'FB'
  static final int PALE_GREEN     = 12; // x'FC' 
  static final int PALE_TURQUOISE = 13; // x'FD'
  static final int GREY           = 14; // x'FE'
  static final int WHITE          = 15; // x'FF'

/*
                                            Html colors #rrggbb
    
    BACKGROUND     =  0;      0000           = black    #000000
    BLUE           =  1;      0001        B  = blue     #0000ff
    RED            =  2;      0010      R    = red      #ff0000
    PINK           =  3;      0011      R+B  = magenta  #ff00ff
    GREEN          =  4;      0100    G      = lime     #00ff00
    TURQUOISE      =  5;      0101    G  +B  = cyan     #00ffff
    YELLOW         =  6;      0110    G+R    = yellow   #ffff00
    FOREGROUND     =  7;      0111    G+R+B  = white    #ffffff
    
    BLACK          =  8;      1000  -        = dim gray #696969
    
    DEEP_BLUE      =  9;      1001  -     B  = navy     #000080
    ORANGE         = 10;      1010  -   R    = maroon   #800000
    PURPLE         = 11;      1011  -   R+B  = purple   #800080
    PALE_GREEN     = 12;      1100  - G      = green    #008000
    PALE_TURQUOISE = 13;      1101  - G  +B  = teal     #008080
    GREY           = 14;      1110  - G+R    = olive    #808000
    WHITE          = 15;      1111  - G+R+B  = gray     #808080

                                             dark gray  #a9a9a9
                                             light gray #d3d3d3
*/

}
