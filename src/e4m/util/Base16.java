package e4m.util;

public final class Base16 {

  public static int hexa(char c) {
    if (c < '0'  ) return 0;      /* x30:x39 */
    if (c < '9'+1) return c - 48; /* 48      */
    if (c < 'A'  ) return 0;      /* x41:x46 */
    if (c < 'F'+1) return c - 55; /* 65 - 10 */
    if (c < 'a'  ) return 0;      /* x61:x66 */
    if (c < 'f'+1) return c - 87; /* 97 - 10 */
    return 0;
  }
  
  public static char hexa(int i) {
    return digit[ i & 0x0f ];
  }

  public static String hex2(int i) {
    return new String(new char[] { digit[ (i >> 4) & 0x0f ], digit[ i & 0x0f ] });
  }

  public static String hex4(int i) {
    return Integer.toHexString( (i & 0x00ffff) | 0x010000 ).substring(1);
  }

  
  static char[] digit = { '0','1','2','3','4','5','6','7',
                          '8','9','A','B','C','D','E','F' };

  public static byte[] decode(CharSequence c) {
    int len = (c.length() - 1) / 3;
    
    if ( c.length() != ((len * 3) + 1) ||
         c.charAt(0) != '[' ||
         c.charAt(len-1) != ']' )
      throw new IllegalArgumentException();

    byte[] b = new byte[len];
    int j = 0;
    int i = 1;
    for (;;) {
      b[j++] = (byte)( (hexa(c.charAt(i++)) << 8) | hexa(c.charAt(i++)) );
      int s = c.charAt(i++);
      if (s == ',') continue;
      if (s == ']' && i == c.length()) break;
      throw new IllegalArgumentException();
    }
    
    return b;
  }
  
  public CharSequence encode(byte[] buf) {
    StringBuilder c = new StringBuilder("[");
    for (byte b : buf) {
      c.append(hexa((int) (b >> 4) ));
      c.append(hexa((int) (b     ) ));
      c.append(',');
    }
    c.setCharAt(c.length()-1,']');
    return c;
  }
  
}
