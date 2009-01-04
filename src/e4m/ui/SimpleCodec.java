package e4m.ui;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import e4m.net.tn3270.datastream.Viewport;

public class SimpleCodec implements Codec {

  CharsetDecoder decoder;
  CharsetEncoder encoder;
  CoderResult result;

  public SimpleCodec(String charsetName) {
    Charset cs = Charset.forName(charsetName);
    this.decoder = cs.newDecoder();
    this.encoder = cs.newEncoder();
  }

  @Override
  public boolean isDBCS(int charsetId) {
    return charsetId >= 0x0F8;
  }
  
  @Override
  public CharsetDecoder decoder(int charsetId) {
    return decoder;
  }

  @Override
  public CharsetEncoder encoder(int charsetId) {
    return encoder;
  }
  
  @Override
  public CoderResult result() {
    return result;
  }

  @Override
  public boolean decode(Viewport f, CharBuffer out) {
    int p, err = 0;
    ByteBuffer in = f.text();
    for (int i = 0; i < f.count(); i++) {
      if (decode( f.getCharsetId(i), in, out, f.start(i), f.end(i) )) {
        if ((p = f.prefix(i)) > 0) {
          out.put(p,(char)0);
        }
        continue;
      } 
      // log decode error
      err++;
    }
    return (err < 1);
  }

  @Override
  public boolean encode(CharBuffer in, Viewport f) {
    int err = 0;
    ByteBuffer out = f.text();
    for (int i = 0; i < f.count(); i++) {
      if (encode( f.getCharsetId(i), in, out, f.start(i), f.end(i) )) {
        continue;
      } 
      // log encode error
      err++;
    }
    return (err < 1);
  }

  @Override
  public boolean decode(int charsetId, ByteBuffer in, CharBuffer out, int start, int end) {
    if (start > end) return false;
    locate(in,out,start,end);
    if (start == end) return true;
    CharsetDecoder dec = decoder(charsetId).reset();
    return (result = dec.decode(in,out,true)).isUnderflow() &&
           (result = dec.flush(out)).isUnderflow();
  }

  @Override
  public boolean encode(int charsetId, CharBuffer in, ByteBuffer out, int start, int end) {
    if (start > end) return false;
    locate(in,out,start,end);
    if (start == end) return true;
    CharsetEncoder enc = encoder(charsetId).reset();
    return (result = enc.encode(in,out,true)).isUnderflow() &&
           (result = enc.flush(out)).isUnderflow();
  }

  static
  private void locate(Buffer in, Buffer out, int start, int end) {
    in.limit(end); in.position(start);
    out.limit(end); out.position(start);
  }
  
}
