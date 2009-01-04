package e4m.ui;

import java.util.Arrays;

import e4m.util.SubSequence;

public class SimpleGrid implements CharGrid {

  public SimpleGrid(int rows, int columns) {
    this.buf = new char[ rows * columns ];
    this.width = columns;
  }
  
  protected char[] buf;
  protected int width;
  
  // CharSequence
  
  public char charAt(int index) {
    return buf[index];
  }
  
  public int length() {
    return buf.length;
  }
  
  public String toString() {
    return new String(buf);
  }
  
  public CharSequence subSequence(int start, int end) {
    if (test(start,end))
      throw new IndexOutOfBoundsException(" "+start+','+end);
    else
      return new SubSequence(buf,start,end);
  }
  
  // CharGrid
  
  public int height() {
    return buf.length / width;
  }
  
  public int width() { 
    return this.width;
  }
  
  public int rowAt(int offset) {
    return offset / width;
  }
  
  public int columnAt(int offset) {
    return offset % width;
  }
  
  public int indexAt(int row, int column) {
    return ( row * width ) + column;  
  }
  
  public char[] copy(int start, int length) {
    return Arrays.copyOfRange(buf,start,start+length);
  }

  boolean test(int start, int end) {
    return 0 > start || start > end || end > buf.length;
  }
  
  boolean test(int start, int end, int length) {
    return test(start,end) || end < (start + length)
                           || buf.length < (start + length);
  }

  public boolean delete(int start, int end, int len, boolean doit) {
    if (test(start,end,len)) return false;
    if (doit) remove(start,end,len);
    return true;
  }
    
  void remove(int start, int end, int len) {
    int i = start;
    int edge = end - len;
    while (i < edge) { buf[i] = buf[i + len]; i++; }
    while (i < end ) { buf[i] = 0;            i++; }
  }  

  public boolean insert(int start, int end, CharSequence text, boolean doit) {
    int len = text.length();
    return test(start,end,len) || insert(start,end,text,0,len,doit);
  }  
    
  boolean insert(int start, int end, CharSequence text, int off, int len, boolean doit) {
    int i = end - 1;
    int edge = i - len;
    while (i > edge) {
      if (buf[ i-- ] != 0) return false;
    }
    if (doit) {
      while (i >= start) {
        buf[i + len] = buf[ i-- ];
      }
      while (len > 0) {
        buf[i + len] = text.charAt(off + (--len));
      }
    }  
    return true;
  }

  public boolean replace(int start, int end, int len, CharSequence text, boolean doit) {
    if (test(start,end,len)) return false;

    int remaining = text.length();
    int gap = remaining - len;
    int edge = start + len + gap;

    if (gap < 0) {
      if (doit) remove( edge, end, -gap );
    }  
    else
      if (gap > 0) {
        if ( edge > end || edge > buf.length
             || ! insert( start+len, end, text, len, gap, doit ) ) {
          return false;
        }  
        remaining -= gap;
      }
    
    if (doit) {
      for (int i = 0; i < remaining; i++) {
        buf[start+i] = text.charAt(i);
      }
    }
    return true;
  }

  public boolean delete(int start, int length) {
    return delete(start,buf.length,length,true);
  }

  public boolean insert(int start, CharSequence text) {
    return insert(start,buf.length,text,true);
  }

  public boolean replace(int start, int length, CharSequence text) {
    return replace(start,buf.length,length,text,true);
  }
  
}
