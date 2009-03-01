package e4m.js.tcp;

import java.io.CharConversionException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataStream {
  
  // java -cp bin DataStream "abcd[123,156]xyz[1,02,003,4,05]jklm/op" out.txt
  
  public static void main(String[] a) throws Exception {
    DataStream ds = new DataStream();
    String str = a[0];
    OutputStream out = new FileOutputStream(a[1]);
    ds.encode(str,out);
    InputStream in = new FileInputStream(a[1]);
    CharSequence result = ds.decode(in);
    System.out.println("-> "+result.toString());
  } 

  CharsetEncoder encoder;

  public DataStream() {
    this(new OutputStreamWriter(System.out).getEncoding());
  }

  public DataStream(String charsetName) {
    encoder = Charset.forName(charsetName).newEncoder();
  }

  Pattern strings = Pattern.compile("\\[|[^\\[]+");
  Pattern numbers = Pattern.compile("\\d{1,3}[,|\\]]");
  
  public void encode(CharSequence in, OutputStream out) throws IOException {
    int last = 0;
    Matcher m = strings.matcher(in);
    do {
      if (m.find()) {
        last = (last < m.start()) ? fail(in,last) : m.end();
        if (in.charAt(m.start()) == '[') {
          m.usePattern(numbers);
          do {
            if (m.find()) {
              last = (last < m.start()) ? fail(in,last) : m.end();
              int n = Integer.parseInt( in.subSequence(m.start(),m.end()-1)
                                          .toString() );
              if (n > 255) fail(in,m.start());
              out.write((byte) n);
              if (in.charAt(m.end()-1) == ']') break;
            }
            else {
              fail(in,m.start());
            }
          } while ( ! m.hitEnd());
          m.usePattern(strings);
        }
        else {
          out.write( encoder.reset()
                            .encode(CharBuffer.wrap(m.group())).array() );
        }
      }
      else {
        fail(in,m.start());
      }
    } while ( ! m.hitEnd());
  }
    
  int fail(CharSequence str, int offset) throws IOException {
    throw new CharConversionException(str+" at "+offset);
  }

  // TODO: decode using Charset from encoder.charset()

  public CharSequence decode(InputStream in) throws IOException {
    StringBuilder buf = new StringBuilder();
    boolean inside = false;
    int c;
    while ((c = in.read()) > -1) {
      if (c < 0x020 || 0x07e < c) {
        if (inside) {
          buf.append(',');
        }
        else {
          buf.append('[');
          inside = true;
        }
        buf.append(Integer.toString(c));
      }
      else {
        if (inside) {
          buf.append(']');
          inside = false;
        }
        buf.append((char) c);
      }
    }  
    return buf;
  }

}

