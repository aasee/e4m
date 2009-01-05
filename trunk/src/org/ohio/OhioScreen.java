package org.ohio;


/**
 *  <code>OhioScreen</code> encapsulates the host presentation space.  The
 *  presentation space is a virtual screen which contains all the
 *  characters and attributes that would be seen on a traditional
 *  emulator screen.  This virtual screen is the primary object for
 *  text-based interactions with the host. The <code>OhioScreen</code> provides
 *  methods that manipulate text, search the screen, send keystrokes
 *  to the host, and work with the cursor.
 *<p>
 *  An <code>OhioScreen</code> object can be obtained from the <code>getScreen()</code> method of
 *  an instance of <code>OhioSession</code>.
 *<p>
 *  The raw presentation space data is maintained in a series of
 *  planes which can be accessed by various methods within this class.
 *  The text plane contains the actual characters in the presentation
 *  space.  Most of the methods in <code>OhioScreen</code> class work exclusively
 *  with the text plane.
 *<p>
 *  The remaining planes contain the corresponding attributes for each
 *  character in the text plane.  The color plane contains color
 *  characteristics.  The field plane contains the field attributes.
 *  The extended plane contains the extended field attributes.  The
 *  color, field, and extended planes are not interpreted by any of
 *  the methods in this class.
 */
public interface OhioScreen {

  /**
   *  The location of the cursor in the presentation space.  The
   *  row and column of the cursor is contained within the
   *  <code>OhioPosition</code> object.
   */
  public OhioPosition getCursor();
 
  /**
   *  The <code>OhioOIA</code> object associated with this presentation space.
   *  This object can be used to query the status of the operator
   *  information area.
   */
  public OhioOIA getOIA();
 
  /**
   *  The <code>OhioFields</code> object associated with this presentation
   *  space.  This provides another way to access the data in the
   *  virtual screen.  The <code>OhioFields</code> object contains a snapshot
   *  of all the fields in the current virtual screen.  Fields
   *  provide methods for interpreting the data in the non-text
   *  planes.  Zero length fields (due to adjacent field
   *  attributes) are not returned in the <code>OhioFields</code> collection.
   *  For unformatted screens, the returned collection contains
   *  only one <code>OhioField</code> that contains the whole virtual screen.
   */
  public OhioFields getFields();
 
  /**
   *  The number of rows in the presentation space.
   */
  public int getRows();
 
  /**
   *  The number of columns in the presentation space.
   */
  public int getColumns();
 
  /**
   *  The entire text plane of the virtual screen as a string.
   *  All null characters and <i>Field Attribute</i> characters are
   *  returned as blank space characters.
   */
  public String getString();
 
  /**
   *  Returns a byte array containing the data from the <i>Text</i>,
   *  <i>Color</i>, <i>Field</i> or <i>Extended</i> plane of the virtual screen.
   *
   *  @param
   *     start  The row and column where to start.  The position
   *            is inclusive (for example, row 1, col 1 means that
   *            position 1,1 will be used as the starting location
   *            and 1,1 will be included in the data).  "start"
   *            must be positionally less than "end".
   *
   *  @param
   *     end    The row and column where to end.  The position is
   *            inclusive (for example, row 1, col 1 means that
   *            position 1,1 will be used as the ending location
   *            and 1,1 will be included in the data). "end" must
   *            be positionally greater than "start".
   *
   *  @param
   *     plane  A valid <code>OHIO_PLANE</code> value.
   */
  public byte[] getData( OhioPosition start,
                         OhioPosition end,
                         Plane plane );

  public enum Plane {
    TEXT,      /* Text (character data) */
    COLOR,     /* Color (standard HLLAPI CGA color values) */
    FIELD,     /* Field Attribute (field attribute bytes) */
    EXTENDED,  /* Extended Attributes (extended attribute bytes) */
  };
  
  public enum Color {
    BLACK,
    BLUE,
    GREEN,
    CYAN,
    RED,
    MAGENTA,
    WHITE,
    YELLOW,
  };
  
  public enum Field {
    ATTRIBUTE,
    PROTECTED,
    NUMERIC,
    PEN_SELECTABLE,
    HIGH_INTENSITY,
    HIDDEN,
    RESERVED,
    MODIFIED,
  };

  public enum Extended {
    HILITE,
    COLOR,
    RESERVED,
  };
  
  public enum ExtendedHilite {
    NORMAL,
    BLINK,
    REVERSEVIDEO,
    UNDERSCORE,
  };  

  public enum ExtendedColor {
    DEFAULT,
    BLUE,
    RED,
    PINK,
    GREEN,
    TURQUOISE,
    YELLOW,
    WHITE,
  };

  /**
   *  Searches the text plane for the target string. If found,
   *  returns an <code>OhioPosition</code> object containing the target
   *  location.  If not found, returns a null.  The <code>targetString</code>
   *  must be completely contained by the target area for the
   *  search to be successful.  Null characters in the text plane
   *  are treated as blank spaces during search processing.
   *
   *  @param
   *     targetString  The target string.
   *
   *  @param
   *     startPos  The row and column where to start.  The
   *               position is inclusive (for example, row 1,
   *               col 1 means that position 1,1 will be used as
   *               the starting location and 1,1 will be included
   *               in the search).
   *
   *  @param
   *     length  The length from startPos to include in the
   *             search.
   *
   *  @param
   *     dir  An <code>OHIO_DIRECTION</code> value.
   *
   *  @param
   *     ignoreCase  Indicates whether the search is case
   *                 sensitive.  True means that case will be
   *                 ignored.  False means the search will be case
   *                 sensitive.
   */
  public OhioPosition findString( String targetString,
                                  OhioPosition startPos,
                                  int length,
                                  Direction dir,
                                  boolean ignoreCase );
  
  public enum Direction {
    FORWARD,  /* Forward (beginning towards end) */
    BACKWARD, /* Backward (end towards beginning) */
  };
  
  /**
   *  Sends a string of keys to the virtual
   *  screen.  This method acts as if keystrokes were being typed
   *  from the keyboard.
   *<p>
   *  The keystrokes will be sent to the location given.  If no
   *  location is provided, the keystrokes will be sent to the
   *  current cursor location.
   *
   *  @param
   *     text  The string of characters to be sent.
   */
  public void sendKeys( String text,
                        OhioPosition location );

  /**
   *  Sends an "AID" keystroke to the virtual
   *  screen.  These aid keys can be though of as special
   *  keystrokes, like the <i>Enter</i> key, the <i>Tab</i> key, or the <i>Page Up</i>
   *  key.  All the valid special key values are contained in the
   *  <code>OHIO_AID</code> enumeration.
   *
   *  @param
   *     aidKey  The <i>AID</i> key to send to the virtual screen.
   */
  public void sendAid( AID aidKey );
 
  public enum AID {
    NONE             ( 0x060 ),
    STRUCTURED_FIELD ( 0x088 ),
    READ_PARTITION   ( 0x061 ),
    TRIGGER_ACTION   ( 0x07F ),
    SYS_REQ          ( 0x0F0 ),
    PF1              ( 0x0F1 ),
    PF2              ( 0x0F2 ),
    PF3              ( 0x0F3 ),
    PF4              ( 0x0F4 ),
    PF5              ( 0x0F5 ),
    PF6              ( 0x0F6 ),
    PF7              ( 0x0F7 ),
    PF8              ( 0x0F8 ),
    PF9              ( 0x0F9 ),
    PF10             ( 0x07A ),
    PF11             ( 0x07B ),
    PF12             ( 0x07C ),
    PF13             ( 0x0C1 ),
    PF14             ( 0x0C2 ),
    PF15             ( 0x0C3 ),
    PF16             ( 0x0C4 ),
    PF17             ( 0x0C5 ),
    PF18             ( 0x0C6 ),
    PF19             ( 0x0C7 ),
    PF20             ( 0x0C8 ),
    PF21             ( 0x0C9 ),
    PF22             ( 0x04A ),
    PF23             ( 0x04B ),
    PF24             ( 0x04C ),
    PA1              ( 0x06C ),
    PA2              ( 0x06E ),
    PA3              ( 0x06B ),
    CLEAR            ( 0x06D ),
    CLEAR_PARTITION  ( 0x06A ),
    ENTER            ( 0x07D ),
    SELECTOR_PEN     ( 0x07E );

    AID(int key) { this.key = key; }
    public int key() { return this.key; }
    private final int key;
  };
  
  /**
   *  Sends a string to the virtual screen at
   *  the specified location.  The string will overlay only
   *  unprotected fields, and any parts of the string which fall
   *  over protected fields will be discarded.
   *
   *  @param
   *     text  String to place in the virtual screen.
   *
   *  @param
   *     location  Position where the string should be written.
   */
  public void setString( String text,
                         OhioPosition location );

}
