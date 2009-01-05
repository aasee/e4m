package org.ohio;

/**
 *  Base class for all Ohio classes.  Contains all common Ohio methods
 *  and properties.
 */
public interface Ohio {


  /**
   *  This enum is used by:
   *  <ul>
   *  <li><code>OHIOSession.connected()</code>
   *  <li><code>OHIOSession <i>SessionChanged</i></code>
   *  </ul>
   */
  public enum State {

    /**
     *  The communication link to the
     *  host is disconnected.
     */
    DISCONNECTED,

    /**
     *  The communication link to the
     *  host is connected.
     */
    CONNECTED,
  };

  /**
   *  This enum is used by:
   *  <ul>
   *  <li><code>OhioScreen <i>event processing</i></code>
   *  </ul>
   */
  public enum Update {

    /**
     *  Update initiated by client
     */
    CLIENT,

    /**
     *  Update initiated by host
     */
    HOST,
  };


  public interface Vendor {

    /**
     *  The OHIO version level of this implementation.
     *  The form is "OHIO nn.nn"
     */
    public String getOhioVersion();

    /**
     *  The name of the vendor providing this
     *  OHIO implementation.  Format is vendor defined.
     */
    public String getVendorName();

    /**
     *  The vendor product version that is providing the
     *  OHIO implementation.  Format is vendor specific.
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
    public OhioPosition createOhioPosition( int row, int col );

  }
}
