package org.ohio;

import java.io.IOException;

/**
 *  A host session.
 */
public interface OhioSession {

  /**
   *  The <i>ConfigurationResource</i> for this <code>OhioSession</code> object.
   */
  public String getConfigurationResource();

  /**
   *  Indicates whether this <code>OhioSession</code> object is connected to a
   *  host.  True means connected, false means not connected.
   */
  public boolean isConnected();

  /**
   *  The <i>SessionName</i> for this <code>OhioSession</code> object.  The
   *  <i>SessionName</i> is unique among all instances of <code>OhioSession</code>.
   */
  public String getSessionName();

  /**
   *  The <i>Session.Type</i> for this <code>OhioSession</code> object.
   */
  public Type getSessionType();
  
  public enum Type {
    UNKNOWN,  /* Unknown host */
    TN3270,   /* 3270 host */
    TN5250    /* 5250 host */
  };

  /**
   *  The <code>OhioScreen</code> object for this session.
   * @throws IOException 
   */
  public OhioScreen getScreen() throws IOException;

  /**
   *  Starts the communications link to the host.
   * @throws IOException 
   */
  public void connect() throws IOException;

  /**
   *  Stops the communications link to the host.
   * @throws IOException 
   */
  public void disconnect() throws IOException;

}
