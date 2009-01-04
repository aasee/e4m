package e4m.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class StandardOutput {
  
  PrintStream stream;
  
  public StandardOutput() {
    this(System.out);
  }

  public StandardOutput(OutputStream out) {
    stream = (out instanceof PrintStream)
                  ? (PrintStream)out : new PrintStream(out);
  }

  public void close() throws IOException {
    stream.close();
  }
  
  public void flush() throws IOException {
    stream.flush();
  }
  
  public void write(byte[] buf, int off, int len)  throws IOException {
    stream.write(buf,off,len);
  }

  public void print(String s) throws IOException {
    stream.println(s);
  }
  
}
