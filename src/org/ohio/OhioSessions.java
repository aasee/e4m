package org.ohio;

/**
 *  Contains a collection of <code>OhioSession</code> objects.
 *  This list is a static snapshot of the list of <code>OhioSession</code>
 *  objects available at the time of the snapshot.
 */
public interface OhioSessions {

  /**
   *  The number of <code>OhioSession</code> objects contained in this collection.
   */
  public int count();

  /**
   *  The <code>OhioSession</code> object at the given index.
   *  "One based" indexing is used in all <i>Ohio</i> collections. For example,
   *  the first <code>OhioSession</code> in this collection is at index 1.
   *
   *  @param
   *     index  The index of the target <code>OhioSession</code>.
   */
  public OhioSession getItem( int index );

  /**
   *  The <code>OhioSession</code> object with the given <i>SessionName</i>.
   *  Returns null if no object with that name exists in this collection.
   *
   *  @param
   *     sessionName  The target name.
   */
  public OhioSession getItem( String sessionName );

  /**
   *  Updates the collection of <code>OhioSession</code> objects.
   *  All <code>OhioSession</code> objects that are available on the system
   *  at the time of the refresh will be added to the collection.
   *  Indexing of <code>OhioSession</code> objects will not be preserved
   *  across refreshes.
   */
  public void refresh();

}
