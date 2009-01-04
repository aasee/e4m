package e4m.net.telnet;

public class TelnetOptions {

  private long[] bits = { 0L, 0L, 0L };

  private final static int CAN = 0;
  private final static int WILL = 1;
  private final static int DO = 2;

  long mask(int i) {
    if (i < 0 || i > 63) throw new IndexOutOfBoundsException(Integer.toString(i));
    return (1L << i);
  }

  boolean set(int b, int i) {
    long mask = mask(i);
    if ((bits[CAN] & mask) == 0L) return false;
    bits[b] |= mask; return true;
  }

  boolean unset(int b, int i) {
    long mask = mask(i);
    if ((bits[CAN] & mask) == 0L) return false;
    bits[b] &= ~ mask; return true;
  }

  boolean test(int b, int i) {
    return ((bits[b] & mask(i)) == 0);
  }

  public void iCan(int i) { bits[CAN] |= mask(i); }
  public void iCanNot(int i) { bits[CAN] &= ~ mask(i); }

  public boolean iWill(int i) { return set(WILL,i); }
  public boolean iWillNot(int i) { return unset(WILL,i); }

  public boolean iDo(int i) { return set(DO,i); }
  public boolean iDoNot(int i) { return unset(DO,i); }

  public boolean iCould(int i) { return test(CAN,i); }
  public boolean iWould(int i) { return test(WILL,i); }
  public boolean iShould(int i) { return test(DO,i); }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    long options = bits[CAN];
    for (int i = 63; i >= 0; i--) {
      if (options < 0) s.append(',').append(Integer.toString(i));
      options <<= 1;
    }
    return s.length() > 0 ? s.substring(1) : "";
  }

  /***
   *   URLConnection request property
   *
   *     Telnet-Options: <list of option numbers in decimal>
   *     Telnet-Options: 0,24,25,40  for TRANSMIT-BINARY,
   *                                     TERMINAL-TYPE,
   *                                     END-OF-RECORD,
   *                                     TN3270E
   */
  public static void set(TelnetConnection c, int ... list) {
    for (int option : list) {
      if (option > -1) c.options.iCan(option);
    }
    c.setHeaderField("Telnet-Options",c.options.toString());
  }


}
