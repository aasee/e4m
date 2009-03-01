package org.ohio;

import java.io.IOException;

/**
 *  The central repository for access to all <i>Ohio</i> sessions.
 *  The <code>OhioManager</code> contains a list of all <code>OhioSession</code>
 *  objects available on this system.
 */
public interface OhioManager {

  /**
   *  An <code>OhioSessions</code> object containing the <code>OhioSession</code>
   *  objects available on this system.  This list of objects is a static
   *  snapshot at the time the <code>OhioSessions</code> object is created.
   *  Use the <code>OhioSessions.refresh()</code> method to obtain a new snapshot.
   */
  public OhioSessions getSessions();

  /**
   *  Returns an <code>OhioSession</code> object based on
   *  the search parameters provided.
   *<p>
   *  The parameters are used as follows:
   *<dl>
   *<p> &nbsp; &nbsp; Is a <i>ConfigurationResource</i> provided?
   *<ul>
   * <li>
   *  <p>   Yes - Is a <i>SessionName</i> provided?
   *  <ul>
   *   <li>
   *    <p>    Yes - Is an <code>OhioSession</code> object with matching
   *                 <i>SessionName</i> available on the system?
   *    <ul>
   *     <li>
   *      <p>     Yes - Error, attempting to create an
   *                    <code>OhioSession</code> object with a non-unique
   *                    <i>SessionName</i>.
   *     <li>
   *      <p>     No - Create an <code>OhioSession</code> object using
   *                   <i>SessionName</i> and <i>ConfigurationResource</i>.
   *    </ul>
   *   <li>
   *    <p>    No - Start a new <code>OhioSession</code> using
   *                <i>ConfigurationResource</i> and generating a new
   *                <i>SessionName</i>.
   *  </ul>
   * <li>
   *  <p>   No - Is a <i>SessionName</i> provided?
   *  <ul>
   *   <li>
   *    <p>    Yes - Is an <code>OhioSession</code> object with matching
   *                 <i>SessionName</i> available on the system?
   *    <ul>
   *     <li>
   *      <p>     Yes - Return identified <code>OhioSession</code> object.
   *     <li>
   *      <p>     No - Return null.
   *    </ul>
   *   <li>
   *    <p>    No - Return null.
   *  </ul>
   *</ul>
   *</dl>
   *
   *  @param
   *     configurationResource  A vendor specific string used to
   *                            provide configuration information.
   *  @param
   *     sessionName  The unique name associated with an <code>OhioSession<code>.
   *
   *  @throws IOException
   */
  public OhioSession openSession( String configurationResource,
                                  String sessionName ) throws IOException;

  /**
   *  Closes an <code>OhioSession</code> object.
   *  The <code>OhioSession</code> is considered invalid and is removed
   *  from the list of <code>OhioSession</code> objects.
   *
   *  @param
   *     sessionObject The <code>OhioSession</code> to close.
   *
   *  @throws IOException
   */
  public void closeSession( OhioSession sessionObject ) throws IOException;

  /**
   *  Closes an </code>OhioSession</code> object.
   *  The <code>OhioSession</code> is considered invalid and is removed
   *  from the list of <code>OhioSession</code> objects.
   *
   *  @param
   *     sessionName The <i>SessionName</i> of the <code>OhioSession</code> to close.
   *               
   *  @throws IOException
   */
  public void closeSession( String sessionName ) throws IOException;

}
