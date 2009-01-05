package org.ohio;

import static org.ohio.OhioScreen.*;

/**
 *  A field is the fundamental element of a virtual screen.  A field
 *  includes both data and attributes describing the field.  The
 *  <code>OhioField</code> class encapsulates a virtual screen field and provides
 *  methods for accessing and manipulating field attributes and data.
 *<p>
 *  <code>OhioField</code> objects can be accessed only through the <code>OhioFields</code>
 *  object.
 */
public interface OhioField {

  /**
   *  The starting position of the field.  The position can range
   *  from 1 to the size of the virtual screen.  The starting
   *  position of a field is the position of the first character
   *  in the field.
   */
  public OhioPosition getStart();
 
  /**
   *  The ending position of the field.  The position can range
   *  from 1 to the size of the virtual screen.  The ending
   *  position of a field is the position of the last character in
   *  the field.
   */
  public OhioPosition getEnd();
 
  /**
   *  The length of the field.  A field's length can range
   *  from 1 to the size of the virtual screen.
   */
  public int getLength();
 
  /**
   *  The attribute bits for the field.
   */
  public int getAttribute();
 
  /**
   *  Indicates whether or not the field has been modified.  True
   *  if the field has been modified, otherwise false.
   */
  public boolean isModified();
 
  /**
   *  Indicates whether or not the field is protected.  True if
   *  the field is protected, otherwise false.
   */
  public boolean isProtected();
 
  /**
   *  Indicates whether or not the field is numeric-only.  True if
   *  the field is numeric only, otherwise false.
   */
  public boolean isNumeric();
 
  /**
   *  Indicates whether or not the field is high-intensity.  True
   *  if the field is high intensity, otherwise false.
   */
  public boolean isHighIntensity();
 
  /**
   *  Indicates whether or not the field is pen-selectable.  True
   *  if the field is pen-selectable, otherwise false.
   */
  public boolean isPenSelectable();
 
  /**
   *  Indicates whether or not the field is hidden.  True if the
   *  field is hidden, otherwise false.
   */
  public boolean isHidden();
 
  /**
   *  The text plane data for the field.  This is similar to the
   *  <code>getData()</code> method using the <code>OHIO_PLANE_TEXT</code> parameter, except
   *  the data is returned as a string instead of a character
   *  array.  When setting the String property, if the string is
   *  shorter than the length of the field, the rest of the field
   *  is cleared.  If the string is longer than the field, the
   *  text is truncated.  A subsequent call to this property will
   *  not reflect the changed text.  To see the changed text, do a
   *  refresh on the <code>OhioFields</code> collection and retrieve a new
   *  <code>OhioField</code> object.
   */
  public String getString();

  /**
   *  Sets the text plane data for the field.
   *
   *  @see <code>getString()</code>
   */
  public void setString( String text );
 
  /**
   *  Returns data from the different planes (text, color, extended)
   *  associated with the field.  The data is returned as a byte array.
   *
   *  @param
   *     targetPlane  An <code>OHIO_PLANE</code> value indicating from which
   *                  plane to retrieve the data.
   */
  public byte[] getData( Plane targetPlane );
 
}
