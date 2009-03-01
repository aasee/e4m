package e4m.ui;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import e4m.net.tn3270.datastream.Text;

public interface Codec {

  CharsetDecoder decoder( int charsetId );
  CharsetEncoder encoder( int charsetId );

  boolean decode( Text in, CharBuffer out );
  boolean encode( CharBuffer in, Text out );

  boolean decode( int charsetId,
                  ByteBuffer in, CharBuffer out,
                  int start, int end);

  boolean encode( int charsetId,
                  CharBuffer in, ByteBuffer out,
                  int start, int end);

  CoderResult result();

  boolean isDBCS( int charsetId );
}
