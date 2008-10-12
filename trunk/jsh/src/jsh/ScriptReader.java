package jsh;

import java.io.Reader;
import java.io.IOException;

public class ScriptReader extends Reader {
  Reader in;
  int slashes;

  public ScriptReader(Reader in) {
    this.in = in;
    slashes = 2;
  }

  public int read(char[] cbuf, int off, int len) throws IOException {
    int n = 0;
    while (slashes > 0 && len > 0) {
      cbuf[off++] = '/';
      n++;
      len--;
      slashes--;
    }
    if (len > 0) {
      int r = in.read(cbuf,off,len);
      if (r < 0)
        return r;
      n += r;
    }
    return n;
  }

  public void close() throws IOException {
    in.close();
  }

}
