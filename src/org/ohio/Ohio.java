package org.ohio;

/**
 *
 *  <h3>draft-ietf-tn3270e-ohio-01.txt</h3>
 *<p>
 *  This draft addresses the need for a common, advanced, client
 *  programming interface to mainframe host data.  Application
 *  developers, in particular third party application vendors, use
 *  API's provided by client emulator programs to write applications
 *  that access host data.  Currently, the defacto standard HLLAPI is
 *  the interface most commonly provided by these programs and the one
 *  used by the third party application vendors.    However, HLLAPI is
 *  plagued by the fact that it doesn't exploit modern programming
 *  techniques and is fragmented by multiple, proprietary
 *  implementations.  Client vendors have, over the last couple of years,
 *  developed more advanced interfaces that exploit modern programming
 *  advances.  However no effort has been put into standardizing these
 *  interfaces and application developers have been forced to either
 *  chose to stay with HLLAPI or perform costly re-implementation of
 *  their products to support each competing advanced client API.
 *<p>
 *  The Open Host Interface Objects (OHIO) address the need for a
 *  standardized advanced programming interface to the host data.  OHIO
 *  does not modify the TN3270/TN5250 protocol or datastream but instead
 *  provides a common access method to that data once it arrives at the
 *  client.  OHIO uses an Object Oriented approach to divide the data into
 *  logical objects, and provides methods on those objects to allow
 *  standard access to the data.  Details about the connection protocol
 *  used to communicate with the host and the specific host platform.
 *<p>
 *  The following is the Ohio containment hierarchy:
 *<dl>
 *  <dl><code>OhioManager</code>  --  Contains one:
 *    <dl><code>OhioSessions</code>  --  Contains a collection of1:
 *      <dl><code>OhioSession</code>  --  Contains one:
 *        <dl><code>OhioScreen</code>  --  Contains one of each of:
 *          <dl><code>OhioOIA</code>  --  The operator information area
 *          <br><code>OhioFields</code>  --  Contains a collection of:
 *            <dl><code>OhioField</code>  --  A field in the presentation space
 *
 *</dl></dl></dl></dl></dl></dl>
 */
public interface Ohio {

  /**
   *  The OHIO version level of this implementation.
   *  The form is "OHIO nn.nn"
   */
  public String getOhioVersion();

  /**
   *  The name of the vendor providing this OHIO implementation.
   *  Format is vendor defined.
   */
  public String getVendorName();

  /**
   *  The vendor product version that is providing the OHIO implementation.
   *  Format is vendor specific.
   */
  public String getVendorProductVersion();

  /**
   *  The vendor object that provides non-standard,
   *  vendor specific extensions to the OHIO object.
   */
  public Object getVendorObject( Object ohioObject );

  /**
   *  Create an OhioPosition object.
   *
   *  @param  row  The row coordinate.
   *  @param  col  The column coordinate.
   */
  public OhioPosition createOhioPosition( final int row, final int col );


  /**
   *  This enum is used by:
   *       <br><code>OhioFields.FindByString</code>
   *       <br><code>OhioScreen.FindString</code>
   */
  public enum Direction {
    FORWARD,  /* Forward (beginning towards end) */
    BACKWARD, /* Backward (end towards beginning) */
  };

  /**
   *  This enum is used by:
   *       <br><code>OhioSession.sessionType</code>
   */
  public enum Type {
    UNKNOWN,  /* Unknown host */
    TN3270,   /* 3270 host */
    TN5250    /* 5250 host */
  };

  /**
   *  This enum is used by
   *       <br><code>OHIOSession.connected()</code>
   *       <br><code>OHIOSession <i>SessionChanged</i></code>
   */
  public enum State {
    CONNECTED,     /* The communication link to the host is connected. */
    DISCONNECTED,  /* The communication link to the host is disconnected. */
  };

  /**
   *  This enum is used by <code>OhioScreen <i>event processing</i></code>
   */
  public enum Update {
    CLIENT,  /* Update initiated by client */
    HOST,    /* Update initiated by host */
  };

  /**
   *  This enum is used by:
   *       <br><code>OHIOField.getData</code>
   *       <br><code>OHIOScreen.getData</code>
   */
  public enum Plane {
    TEXT,      /* Text (character data) */
    COLOR,     /* Color (standard HLLAPI CGA color values) */
    FIELD,     /* Field Attribute (field attribute bytes) */
    EXTENDED,  /* Extended Attributes (extended attribute bytes) */
  };

  /**
   *  These values are returned in the Ohio Color Plane from the following methods:
   *        <br><code>OhioField.getData</code>
   *        <br><code>OhioScreen.getData</code>
   */
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

  /**
   *  These values are returned in the Ohio Field Attributes from the following methods:
   *        <br><code>OhioScreen.getData</code>
   */
  public enum Field {
    ATTRIBUTE,
    PROTECTED,
    NUMERIC,
    PEN_SELECTABLE,
    HIGH_INTENSITY,
    HIDDEN,
    MODIFIED,
  };

  /**
   *  These values are returned in the Ohio Extended Field Plane from the following methods:
   *        <br><code>OhioField.getData</code>
   *        <br><code>OhioScreen.getData</code>
   */
  public enum Extended {
    HILITE,
    COLOR,
  };

  /**
   *  Additional Extended.HILITE bits
   */
  public enum ExtendedHilite {
    NORMAL,
    BLINK,
    REVERSEVIDEO,
    UNDERSCORE,
  };

  /**
   *  Additional Extended.COLOR bits
   */
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
   *  This enum is used by:
   *       <br><code>OhioScreen.sendAID</code>
   */
  public enum AID {
    NONE,
    STRUCTURED_FIELD,
    READ_PARTITION,
    TRIGGER_ACTION,
    SYS_REQ,
    PF1,
    PF2,
    PF3,
    PF4,
    PF5,
    PF6,
    PF7,
    PF8,
    PF9,
    PF10,
    PF11,
    PF12,
    PF13,
    PF14,
    PF15,
    PF16,
    PF17,
    PF18,
    PF19,
    PF20,
    PF21,
    PF22,
    PF23,
    PF24,
    PA1,
    PA2,
    PA3,
    CLEAR,
    CLEAR_PARTITION,
    ENTER,
    SELECTOR_PEN,
  };

}
