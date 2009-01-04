package e4m.util;

public class SubSequence implements CharSequence {

  char[] buf;
  int start, end;
  
  public SubSequence(char[] buf, int start, int end) {
    this.buf = buf;
    this.start = start;
    this.end = end;
  }
  
  public int length() { return (end - start); }

  @Override
  public String toString() { return new String(buf,start,length()); }

  public char charAt(int index) {
    if (-1 < index && index < length()) {
      return buf[start+index];
    }  
    throw new IndexOutOfBoundsException(" "+index);
  }

  public CharSequence subSequence(int start, int end) {
    if (-1 < start && start <= end && end < length()) {
      return new SubSequence( buf, this.start + start,
                                    this.start + (end - start) );
    }
    throw new IndexOutOfBoundsException(" "+start+':'+end);
  }
  
}
