package org.ohio;

/**
 *  Holds row and column coordinates.  <i>An OhioPosition can be
 *  constructed by using CreateOhioPosition() on any Ohio class.</i>
 */

public interface OhioPosition {

  /**
   *  The row coordinate
   */
  public int row();

  /**
   *  The column coordinate.
   */
  public int column();

}
