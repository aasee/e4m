package e4m.net.tn3270.ds;

class Attribute {
  
  //  NOTE:  '3270 Field' attribute bits are not stored with the
  //         'Extended Field' attribute bits

  /**
   *   0....+....1....+....2....+....3..
   *
   *                              .... .   3270 Field                   C0
   *
   *                              00         no display position
   *                              1          protected
   *                               1         numeric
   *                              11         skip
   *                                00       display,     not detectable
   *                                01       display,     detectable
   *                                10       intensified, detectable
   *                                11       non-display, non-detectable
   *                                   1     MDT
   *
   *   0....+....1....+....2....+....3..
   *
   *                                  ..   Transparency                 46
   *
   *                                  00     OR
   *                                  01     XOR
   *                                  11     non-transparent
   *
   *                              ....     Outlining                    C2
   *
   *                                 1       bottom
   *                                1        right
   *                               1         top
   *                              1          left
   *
   *                           ...         Validation                   C1
   *
   *                           1             mandatory fill
   *                            1            mandatory entry
   *                             1           trigger
   *
   *                        ...            Extended Highlighting        41
   *
   *                        001              normal
   *                        011              blink
   *                        101              reverse video
   *                        111              underscore
   *
   *                ........               Character Set                43
   *
   *                11111111                 local id
   * 
   *           .....                       Foreground Color             42
   *
   *           11111                         color index (0-16)
   *
   *      .....                            Background Color             45
   *
   *      11111                              color index (0-16)
   *      
   *     .                                 Attribute byte indicator
   *     1                                   boolean 
   *
   *    10000000000000000000000000000000   unused slot (Integer.MIN_VALUE)
   *
   *    ..3....+....2....+....1....+....0
   */

  static final int NIL = Integer.MIN_VALUE;
  
  static final int occupied = 0x40000000;
  
  static int setOccupied(int bits, boolean state) {
    return state ? (bits | 0x40000000) : (bits & 0xbfffffff);
  }
  
  static boolean occupied(int bits)   { return (bits & 0x40000000) != 0; }
  
  static int setFieldAttribute(int bits, int mark) {
    return (bits & 0xffffffc2) | (mark & 0x0000003d);
  }

  static int getFieldAttribute(int bits) {
    return (bits & 0x0000003d);
  }

  static boolean immutable(int bits)  { return (bits & 0x020) == 0x020; }
  static boolean mutable(int bits)    { return (bits & 0x020) != 0x020; }
  static boolean numeric(int bits)    { return (bits & 0x010) == 0x010; }
  static boolean skipped(int bits)    { return (bits & 0x030) == 0x030; }
  static boolean visible(int bits)    { return (bits & 0x00c) != 0x00c; }
  static boolean bright(int bits)     { return (bits & 0x00c) == 0x008; }
  static boolean hidden(int bits)     { return (bits & 0x00c) == 0x00c; }
  static boolean modified(int bits)   { return (bits & 0x001) == 0x001; }

  static boolean selectable(int bits) {
    int b = (bits & 0x00c); return (b == 0x004) || (b == 0x008);
  }

  static int modifyTag(int bits, boolean mdt) {
    return mdt ? (bits |= 0x00000001) : (bits &= 0xfffffffe);
  }

  static int setFieldTransparency(int bits, int mark) {
    switch (mark) {
      default: throw new IllegalArgumentException("field transparency: "+toHex(mark));

      case 0x000: return (bits & 0xfffffffc);
      case 0x0f0: return (bits & 0xfffffffc) | 0x00000001;
      case 0x0f1: return (bits & 0xfffffffc) | 0x00000002;
      case 0x0ff: return (bits & 0xfffffffc) | 0x00000003;
    }
  }

  static int getFieldTransparency(int bits) {
    switch (bits & 0x00000003) {
      default:         return 0x000;
      case 0x00000001: return 0x0f0;
      case 0x00000002: return 0x0f1;
      case 0x00000003: return 0x0ff;
    }
  }

  static boolean XORed(int bits)       { return (bits & 0x00000003) == 0x00000001; }
  static boolean ORed(int bits)        { return (bits & 0x00000003) == 0x00000002; }
  static boolean transparent(int bits) { return (bits & 0x00000003) == 0x00000003; }

  static int setFieldOutlining(int bits, int mark) {
    if (mark > 0x00f) {
      throw new IllegalArgumentException("field outlining: "+toHex(mark));
    }
    return (bits & 0xffffffc3) | (mark << 2);
  }

  static int getFieldOutlining(int bits) { return (bits & 0x0000003c) >>> 2; }

  static int setFieldValidation(int bits, int mark) {
    if (mark > 0x003) {
      throw new IllegalArgumentException("field validation: "+toHex(mark));
    }
    return (bits & 0xfffffe3f) | (mark << 6);
  }

  static int getFieldValidation(int bits) { return (bits & 0x000001c0) >>> 6; }

  static boolean mandatoryFill(int bits)  { return (bits & 0x000001c0) == 0x00000040; }
  static boolean mandatoryEntry(int bits) { return (bits & 0x000001c0) == 0x00000080; }
  static boolean trigger(int bits)        { return (bits & 0x000001c0) == 0x00000100; }

  static int setExtendedHighlighting(int bits, int mark) {
    switch (mark) {
      default: throw new IllegalArgumentException("extended highlighting: "+toHex(mark));

      case 0x000: return (bits & 0xfffff1ff);
      case 0x0f0: return (bits & 0xfffff1ff) | 0x000000e00;
      case 0x0f1: return (bits & 0xfffff1ff) | 0x000000200;
      case 0x0f2: return (bits & 0xfffff1ff) | 0x000000400;
      case 0x0f4: return (bits & 0xfffff1ff) | 0x000000800;
    }
  }

  static int getExtendedHighlighting(int bits) {
    switch (bits & 0x00000e00) {
      default:         return 0x000;
      case 0x00000e00: return 0x0F0;
      case 0x00000200: return 0x0F1;
      case 0x00000400: return 0x0F2;
      case 0x00000800: return 0x0F4;
    }
  }

  static boolean highlighted(int bits) { return (bits & 0x00000e00) == 0x00000e00; }
  static boolean blinking(int bits)    { return (bits & 0x00000e00) == 0x00000200; }
  static boolean reversed(int bits)    { return (bits & 0x00000e00) == 0x00000400; }
  static boolean underscored(int bits) { return (bits & 0x00000e00) == 0x00000800; }

  static int setCharacterSetId(int bits, int mark) {
    if (mark > 0 && (mark < 0x040 || mark > 0x0fe)) {
      throw new IllegalArgumentException("character set id: "+toHex(mark));
    }
    return (bits & 0xfff00fff) | (mark << 12);
  }

  static int getCharacterSetId(int bits) { return (bits & 0x000ff000) >>> 12; }

  static int setForegroundColor(int bits, int mark) {
    if (mark > 0 && (mark < 0x0F0 || mark > 0x0ff)) {
      throw new IllegalArgumentException("foreground color: "+toHex(mark));
    }
    return (bits & 0xfe0fffff) | ((mark & 0x01f) << 20);
  }

  static int getForegroundColor(int bits) {
    int c = foreground(bits);
    return (c > 0) ? (c | 0x0f0) : c;
  }

  static int foreground(int bits) { return ((bits & 0x01f00000) >>>  20); }

  static int setBackgroundColor(int bits, int mark) {
    if (mark > 0 && (mark < 0x0f0 || mark > 0x0ff)) {
      throw new IllegalArgumentException("background color: "+toHex(mark));
    }
    return (bits & 0x3effffff) | ((mark & 0x01f) << 25);
  }

  static int getBackgroundColor(int bits) {
    int c = background(bits);
    return (c > 0) ? (c | 0x0f0) : c;
  }

  static int background(int bits) { return ((bits & 0x3e000000) >>> 25); }

  static final String toHex(int i) {
    return Integer.toHexString((i&255)+256).substring(1);
  }

}
