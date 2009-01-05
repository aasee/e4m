package org.ohio;


/**
 *  The operator information area of a host session.  This area is
 *  used to provide status information regarding the state of the host
 *  session and location of the cursor.
 *<p>
 *  An <code>OhioOIA</code> object can be obtained using the <code>getOIA()</code> method on an
 *  instance of <code>OhioScreen</code>.
 */
public interface OhioOIA {

  /**
   *  Indicates whether the field which contains the cursor is an
   *  alphanumeric field.  True if the cursor is in an
   *  alphanumeric field, false otherwise.
   */
  public boolean isAlphanumeric();

  /**
   *  The communication check code.  If <code>getInputInhibitedType</code> returns
   *  <code>OHIO_INPUTINHIBITED_COMMCHECK</code>, this property will return the
   *  communication check code.
   */
  public int getCommCheckCode();

  /**
   *  Indicates whether or not input is inhibited.  If input is
   *  inhibited, <code>sendKeys()</code> or <code>sendAID()</code> calls to the <code>OhioScreen</code> are
   *  not allowed.  Why input is inhibited can be determined from
   *  the value returned.  If input is inhibited for more than one
   *  reason, the highest value is returned.
   */
  public InputInhibited getInputInhibitedType();
  
  public enum InputInhibited {
    NOTINHIBITED, /* Input not inhibited */
    SYSTEM_WAIT,  /* Input inhibited by a System Wait state ("X SYSTEM" or "X []") */
    COMMCHECK,    /* Input inhibited by a communications check state ("X COMMxxx") */
    PROGCHECK,    /* Input inhibited by a program check state ("X PROGxxx") */
    MACHINECHECK, /* Input inhibited by a machine check state ("X MACHxxx") */
    OTHER,        /* Input inhibited by something other than above states */
  };

  /**
   *  The machine check code.  If <code>getInputInhibitedType()</code> returns
   *  <code>OHIO_INPUTINHIBITED_MACHINECHECK</code>, this property will return
   *  the machine check code.
   */
  public int getMachineCheckCode();

  /**
   *  Indicates whether the field which contains the cursor is a
   *  numeric-only field.  True if the cursor is in a numeric-only
   *  field, false otherwise.
   */
  public boolean isNumeric();

  /**
   *  Indicates the owner of the host connection.
   */
  public Owner getOwner();

  public enum Owner {
    UNKNOWN,
    APP,      /* Application or 5250 host */
    MYJOB,    /* 3270 - Myjob */
    NVT,      /* 3270 in NVT mode */
    UNOWNED,  /* 3270 - Unowned */
    SSCP,     /* 3270 - SSCP */
  };
  
  /**
   *  The program check code.  If <code>getInputInhibitedType()</code> returns
   *  <code>OHIO_INPUTINHIBITED_PROGCHECK</code>, this property will return the
   *  program check code.
   */
  public int getProgCheckCode();

};
