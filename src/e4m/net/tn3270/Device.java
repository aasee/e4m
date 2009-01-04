    package e4m.net.tn3270;

public class Device {

  public enum Model {
    IBM_3277_2    ( 24,  80,  0 ),
    IBM_3278_2    ( 24,  80,  4 ),
    IBM_3278_2_E  ( 24,  80, 16 ),
    IBM_3278_3    ( 32,  80,  4 ),
    IBM_3278_3_E  ( 32,  80, 16 ),
    IBM_3278_4    ( 43,  80,  4 ),
    IBM_3278_4_E  ( 43,  80, 16 ),
    IBM_3278_5    ( 27, 132,  4 ),
    IBM_3278_5_E  ( 27, 132, 16 );

    private Model(int r, int c, int l) {
      rows=r; columns=c; colors=l;
    }
    private int rows, columns, colors;

    public final int rows() { return rows; }
    public final int columns() { return columns; }
    public final int colors() { return colors; }
  }

  public static Model forName(String name) {
    try { return Enum.valueOf(Model.class,name.replace('-','_')); }
      catch (Exception e) { return null; }
  }

  final static int LEN = 0;
  final static int SFID = 2;
  final static int QCODE = 3;
  final static int LIST = 4;

  public static byte[][] QueryReply() {
    byte[][] reply = new byte[][]
    {
      new byte[] {0,0,0,(byte)0x080},
      QR_UsableArea(),
      QR_AlphanumericPartitions(),
      QR_CharacterSets(),
      QR_Color(),
      QR_Highlighting(),
      QR_ReplyModes(),
      QR_RPQNames(),
      QR_ImplicitPartition()
    };

    reply[0] = QR_Summary(reply);
    return reply;
  }

  static byte[] QR_Summary(byte[][] reply) {
    byte[] text = new byte[ LIST + reply.length ];

    text[LEN+0] = (byte) 0;
    text[LEN+1] = (byte) text.length;
    text[SFID ] = (byte) 0x081;
    text[QCODE] = (byte) 0x080;

    for (int i = 0; i < reply.length; i++) {
      text[LIST+i] = reply[i][QCODE];
    }
    return text;
  }

  static byte[] QR_UsableArea() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x17,
      (byte) 0x81, (byte) 0x81,
      (byte) 0x01,
      (byte) 0x00,
      (byte) 0x00, (byte) 0x50,
      (byte) 0x00, (byte) 0x18,
      (byte) 0x01,
      (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01,
      (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x01,
      (byte) 0x07,
      (byte) 0x07,
      (byte) 0x07, (byte) 0x80
    };
  }

  static byte[] QR_AlphanumericPartitions() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x08,
      (byte) 0x81, (byte) 0x84,
      (byte) 0x00,
      (byte) 0x07, (byte) 0x80,
      (byte) 0x00
    };
  }

  static byte[] QR_CharacterSets() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x1b,
      (byte) 0x81, (byte) 0x85,
      (byte) 0x82,
      (byte) 0x00,
      (byte) 0x07,
      (byte) 0x07,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x07,
      (byte) 0x00,
      (byte) 0x10,
      (byte) 0x00,
      (byte) 0x02, (byte) 0xb9, (byte) 0x00, (byte) 0x25,
      (byte) 0x01,
      (byte) 0x10,
      (byte) 0xf1,
      (byte) 0x03, (byte) 0xc3, (byte) 0x01, (byte) 0x36
    };
  }

  static byte[] QR_Color() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x1a,
      (byte) 0x81, (byte) 0x86,
      (byte) 0x00,
      (byte) 0x08,
      (byte) 0x00, (byte) 0xf4,
      (byte) 0xf1, (byte) 0xf1,
      (byte) 0xf2, (byte) 0xf2,
      (byte) 0xf3, (byte) 0xf3,
      (byte) 0xf4, (byte) 0xf4,
      (byte) 0xf5, (byte) 0xf5,
      (byte) 0xf6, (byte) 0xf6,
      (byte) 0xf7, (byte) 0xf7,
      (byte) 0x04,
      (byte) 0x02,
      (byte) 0x00, (byte) 0xf0
    };
  }

  static byte[] QR_Highlighting() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x0f,
      (byte) 0x81, (byte) 0x87,
      (byte) 0x05,        
      (byte) 0x00, (byte) 0xf0,
      (byte) 0xf1, (byte) 0xf1,
      (byte) 0xf2, (byte) 0xf2,
      (byte) 0xf4, (byte) 0xf4,
      (byte) 0xf8, (byte) 0xf8
    };
  }

  static byte[] QR_ReplyModes() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x07,
      (byte) 0x81, (byte) 0x88,
      (byte) 0x00,
      (byte) 0x01,
      (byte) 0x02
    };
  }

  static byte[] QR_RPQNames() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x12,
      (byte) 0x81, (byte) 0xa1,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x06,
      (byte) 0xa7, (byte) 0xf3, (byte) 0xf2, (byte) 0xf7, (byte) 0xf0
    };
  }

  static byte[] QR_ImplicitPartition() {
    return new byte[]
    {
      (byte) 0x00, (byte) 0x11,
      (byte) 0x81, (byte) 0xa6,
      (byte) 0x00, (byte) 0x00,
      (byte) 0x0b,
      (byte) 0x01,
      (byte) 0x00,
      (byte) 0x00, (byte) 0x50,
      (byte) 0x00, (byte) 0x18,
      (byte) 0x00, (byte) 0x50,
      (byte) 0x00, (byte) 0x18
    };
  }

}
