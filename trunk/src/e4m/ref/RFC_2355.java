package e4m.ref;

public interface RFC_2355 {

  /* RFC 2355 - TN3270 Enhancements */

  static final int  TN3270E            =  40;

  /* TN3270E verb codes */

  static final int  ASSOCIATE          =    0;
  static final int  CONNECT            =    1;
  static final int  DEVICE_TYPE        =    2;
  static final int  FUNCTIONS          =    3;
  static final int  IS                 =    4;
  static final int  REASON             =    5;
  static final int  REJECT             =    6;
  static final int  REQUEST            =    7;
  static final int  SEND               =    8;

  /* TN3270E Reason-codes */

  static final int  CONN_PARTNER       =    0;
  static final int  DEVICE_IN_USE      =    1;
  static final int  INV_ASSOCIATE      =    2;
  static final int  INV_NAME           =    3;
  static final int  INV_DEVICE_TYPE    =    4;
  static final int  TYPE_NAME_ERROR    =    5;
  static final int  UNKNOWN_ERROR      =    6;
  static final int  UNSUPPORTED_REQ    =    7;

  /* TN3270E Function Names */

  static final int  BIND_IMAGE         =    0;
  static final int  DATA_STREAM_CTL    =    1;
  static final int  RESPONSES          =    2;
  static final int  SCS_CTL_CODES      =    3;
  static final int  SYSREQ             =    4;


  /* TN3270E Message Header */

  interface MH {

    //  ----------------------------------------------------------
    //  | DATA-TYPE | REQUEST-FLAG | RESPONSE-FLAG |  SEQ-NUMBER |
    //  ----------------------------------------------------------
    //     1 byte        1 byte         1 byte         2 bytes
  
    static final int   LENGTH              = 5;
  
    static final int   DATA_TYPE           = 0;
    static final int   REQUEST_FLAG        = 1;
    static final int   RESPONSE_FLAG       = 2;
    static final int   SEQ_NUMBER          = 3;

    // DATA-TYPE Field

    static final byte  S3270_DATA          = 0x00;
    static final byte  SCS_DATA            = 0x01;
    static final byte  RESPONSE            = 0x02;
    static final byte  BIND_IMAGE          = 0x03;
    static final byte  UNBIND              = 0x04;
    static final byte  NVT_DATA            = 0x05;
    static final byte  REQUEST             = 0x06;
    static final byte  SSCP_LU_DATA        = 0x07;
    static final byte  PRINT_EOJ           = 0x08;

    // REQUEST-FLAG Field

    static final byte  ERR_COND_CLEARED    = 0x00;

    // RESPONSE-FLAG Field, DATA-TYPE = 3270-DATA or SCS-DATA

    static final byte  NO_RESPONSE         = 0x00;
    static final byte  ERROR_RESPONSE      = 0x01;
    static final byte  ALWAYS_RESPONSE     = 0x02;

    // RESPONSE-FLAG Field, DATA-TYPE = RESPONSE-DATA

    static final byte  POSITIVE_RESPONSE   = 0x00;
    static final byte  NEGATIVE_RESPONSE   = 0x01;
  }

}
