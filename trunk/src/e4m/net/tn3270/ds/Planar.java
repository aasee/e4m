package e4m.net.tn3270.ds;

import java.util.Arrays;
import e4m.util.IntPool;

class Planar extends IntPool {

  byte[] text;
  
  int position, cursorPosition;
  
  Planar(int size) {
    super(size, Attribute.NIL);
    text = new byte[size];
    position = cursorPosition = 0;
  }
  
  Planar(int rows, int columns) {
    this( rows * columns );
  }
  
  int length() {
    return text.length;
  }
 
  int position() {
    return position;
  }

  void position(int address) {
    position = address;
    if (position >= text.length) {
      position %= text.length;
    }
  }

  void next() {
    position( position + 1 );
  }

  int cursorPosition() {
    return cursorPosition;
  }
  
  void markCursor() {
    cursorPosition = position;
  }
  
  void putByte(int code) {
    text[position] = (byte) code;
    remove(position);
  }

  void markField(int mark) {
    text[position] = (byte) mark;
  }
  
  void putAttribute(int attribute) {
    put(position,attribute);
  }

  int getAttribute() {
    return get(position);
  }
  
  void reset() {
    super.clear();
    Arrays.fill(text,(byte)0);
    position = cursorPosition = 0;
  }

  Object[] getView() {
    short[] start = new short[ used + 1 ];
    int[] attribute = new int[ used ];

    int nth = 0;
    for (int index = 0; index < indirect.length; index++) {
      if (indirect[index] > 0) {
        start[nth] = (short) index;
        attribute[nth] = pool[ indirect[index] - 1 ];
        nth += 1;
      }
    }

    start[nth] = (short) text.length;

    return new Object[] {start,attribute};
  }

}