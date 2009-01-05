package org.ohio;

import static org.ohio.OhioScreen.*;

/**
 *  <code>OhioFields</code> contains a collection of the fields in the virtual
 *  screen.  It provides methods to iterate through the fields, find
 *  fields based on location, and find fields containing a given
 *  string.  Each element of the collection is an instance of
 *  <code>OhioField</code>.
 *<p>
 *  <code>OhioFields</code> can only be accessed through <code>OhioScreen</code> using the
 *  <code>fields()</code> method.  <code>OhioFields</code> is a static view of the virtual
 *  screen and does not reflect changes made to the virtual screen
 *  after its construction.  The field list can be updated with a new
 *  view of the virtual screen using the <code>refresh()</code> method.
 *<p>
 *  <b>Note:</b>  All <code>OhioField</code> objects returned by methods in this class are
 *  invalidated when <code>refresh()</code> is called.
 */
public interface OhioFields {

  /**
   *  Returns the number of <code>OhioField</code> objects contained in this
   *  collection.
   */
  public int count();
 
  /**
   *  Returns the <code>OhioField</code> object at the given index.  "One
   *  based" indexing is used in all <code>Ohio</code> collections.  For
   *  example, the first <code>OhioField</code> in this collection is at
   *  index 1.
   */
  public OhioField getItem( int fieldIndex );
 
  /**
   *  Updates the collection of <code>OhioField</code> objects.  All <code>OhioField</code>
   *  objects in the current virtual screen are added to the
   *  collection.  Indexing of <code>OhioField</code> objects will not be
   *  preserved across refreshes.
   */
  public void refresh();
 
  /**
   *  Searches the collection for the target string and returns
   *  the <code>OhioField</code> object containing that string.  The string
   *  must be totally contained within the field to be considered
   *  a match.  If the target string is not found, a null will be
   *  returned.
   *
   *  @param
   *     targetString  The target string.
   *
   *  @param
   *     startPos      The row and column where to start.  The
   *                   position is inclusive (for example, row 1,
   *                   col 1 means that position 1,1 will be used
   *                   as the starting location and 1,1 will be
   *                   included in the search).
   *
   *  @param
   *     length        The length from <code>startPos</code> to include in the
   *                   search.
   *
   *  @param
   *     dir           An <code>OHIO_DIRECTION</code> value.
   *
   *  @param
   *     ignoreCase    Indicates whether the search is case
   *                   sensitive.  True means that case will be
   *                   ignored.  False means the search will be
   *                   case sensitive.
   */
  public OhioField findByString( String targetString,
                                 OhioPosition startPos,
                                 int length,
                                 Direction dir,
                                 boolean ignoreCase );
 
  /**
   *  Searches the collection for the target position and returns
   *  the OhioField object containing that position.  If not
   *  found, returns a null.
   *
   *  @param
   *     targetPosition  The target row and column.
   */
  public OhioField findByPosition( OhioPosition targetPosition );
 
}
