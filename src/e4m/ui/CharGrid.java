package e4m.ui;

public interface CharGrid extends CharSequence {

  public int height();              // number of rows
  public int width();               // number of columns
  
  public int rowAt(int offset);     // row index for absolute offset
  public int columnAt(int offset);  // column index for absolute offset
  
  public int indexAt(int row, int column);
  
  public char[] copy(int start, int length);
  
  public boolean delete(int start, int length);
  public boolean insert(int start, CharSequence text);
  public boolean replace(int start, int length, CharSequence text);
  
  public boolean delete(int start, int end, int len, boolean doit);
  public boolean insert(int start, int end, CharSequence text, boolean doit);
  public boolean replace(int start, int end, int len, CharSequence text, boolean doit);
  
}
