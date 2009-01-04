package e4m.io;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

/*
   0----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----
     000000  00 01 02 03 04 05 06 07  08 09 0a 0b 0c 0d 0e 0f  |................|
*/

public class SnapInputStream extends InputStream {

  BufferedReader in;
  String line;
  int index;

  final int MIN = 10;
  final int MAX = 56;
  final int MID = 34;
  final int INC = 3;

  public SnapInputStream(Reader in) {
    this.in = (in instanceof BufferedReader) ? (BufferedReader)in : new BufferedReader(in);
    index = MAX;
  }

  public int read() throws IOException {
    for (;;) {
      index += INC;
      if (index == MID) index++;
      if (index > MAX || index > line.length()) {
        line = in.readLine();
        if (line == null) {
          return -1;
        }
        index = MIN;
      }
      if (! Character.isWhitespace(line.charAt(index)))
        return Integer.parseInt(line.substring(index,index+2),16) & 0x00ff;
    }
  }

}
