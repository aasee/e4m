package e4m.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StandardInput {
  
  BufferedReader reader;
  InputStream stream;
  
  public StandardInput() {
    this(System.in);
  }

  public StandardInput(InputStream in) {
    stream = in;
    reader = new BufferedReader(new InputStreamReader(stream)); 
  }

  public void close() throws IOException {
    reader.close();
  }
  
  public void reset() throws IOException {
    reader.reset();
  }

  public boolean ready() throws IOException {
    return reader.ready();
  }

  public String scan() throws IOException {
    return reader.readLine();
  }
  
  public int read(byte[] buf, int off, int len) throws IOException {
    return stream.read(buf,off,len);
  } 
  
  public long skip(long n) throws IOException {
    return stream.skip(n);
  }
  
  public int available() throws IOException {
    return stream.available();
  }

}
