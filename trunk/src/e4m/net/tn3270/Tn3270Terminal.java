package e4m.net.tn3270;

import e4m.net.Pty;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import e4m.net.tn3270.datastream.Text;
import e4m.net.tn3270.datastream.Viewport;
import e4m.net.Vty;
import e4m.ui.SimpleCodec;
import e4m.ref.AID;

import static e4m.ref.RFC_2355.*;
import static e4m.ref.STD_0008.*;
import static e4m.ref.GA23_0059.*;

public class Tn3270Terminal implements Pty {
  
  static {
    System.setProperty("java.protocol.handler.pkgs","e4m.net");
    System.setProperty("java.content.handler.pkgs","e4m.net");
  }

  protected Tn3270Connection urlc;

  public void poll(Vty vty) throws IOException {
    while (connected()) {
      urlc.getInputStream().reset();
      Object o = urlc.getContent();
      if (o instanceof Viewport) {
        Viewport p = (Viewport)o;

        if (autoAcknowledge()) {
          byte[] b = p.header();
          if (b[MH.RESPONSE_FLAG] == MH.ALWAYS_RESPONSE) {
            acknowledge(b);
          }
        }

        Text fields = p.fields();
        if (fields != null) {
          vty.reset(p.restoreKeyboard(),p.resetModifiedDataTags());
          vty.update(p.command(),p.cursor(),fields);
        }
        else {
          processDataStream(p.command(),p.datastream());
        }
      }
    }
  }

  public void configure(Vty vty) {
    setSize(vty);
    setCodec(vty);
    setListener(vty);
  }
   
  void setListener(Vty vty) {
    vty.setAttentionListener(
      new Vty.AttentionListener() {
        public void attention(int aid, int cursor, Text fields) {
          processAttention(aid,cursor,fields);
        }
      });
  }
  
  void setCodec(Vty vty) {
    String contentEncoding = urlc.getContentEncoding();
    vty.setCodec(new SimpleCodec(contentEncoding));
  }  
  
  void setSize(Vty vty) {
    Device.Model mod = Device.forName(urlc.getHeaderField("Device-Type"));
    vty.setSize(mod.rows(),mod.columns());   
  }

  
  public void connect(String url) throws IOException {
    this.urlc = (Tn3270Connection) new URL(url).openConnection();
  }

  public void disconnect() throws IOException {
    urlc.getInputStream().close();
    urlc.getOutputStream().close();
    urlc = null;
  }
  
  public URLConnection connection() {
    return urlc;
  }
  
  public boolean connected() {
    return (urlc != null);
  }
  
  boolean autoAcknowledge = true;

  public boolean autoAcknowledge() {
      return this.autoAcknowledge;
  }

  public void autoAcknowledge(boolean state) {
      this.autoAcknowledge = state;
  }

  public void acknowledge(byte[] b) throws IOException {
    urlc.writeBytes( MH.RESPONSE, 0,
                     MH.POSITIVE_RESPONSE,
                     b[MH.SEQ_NUMBER + 0],
                     b[MH.SEQ_NUMBER + 1],
                     0, IAC, EOR );
    // TODO: other acknowledge-ments
  }

  int responseSequence = 0;

  public void processAttention(int aid, int cursor, Text fields) {
    try {
      sendHeader();

      urlc.writeByte(aid);
      urlc.writeBytes(address(cursor));
      if (aid == AID.ENTER) {
        sendModified(fields);
      }

      sendTrailer();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  static byte[] twelve =
    new byte[] {
      /* 00 - 03 */ (byte)0x040, (byte)0x0C1, (byte)0x0C2, (byte)0x0C3,
      /* 04 - 07 */ (byte)0x0C4, (byte)0x0C5, (byte)0x0C6, (byte)0x0C7,
      /* 08 - 0b */ (byte)0x0C8, (byte)0x0C9, (byte)0x04A, (byte)0x04B,
      /* 0c - 0f */ (byte)0x04C, (byte)0x04D, (byte)0x04E, (byte)0x04F,
      /* 10 - 13 */ (byte)0x050, (byte)0x0D1, (byte)0x0D2, (byte)0x0D3,
      /* 14 - 17 */ (byte)0x0D4, (byte)0x0D5, (byte)0x0D6, (byte)0x0D7,
      /* 18 - 1b */ (byte)0x0D8, (byte)0x0D9, (byte)0x05A, (byte)0x05B,
      /* 1c - 1f */ (byte)0x05C, (byte)0x05D, (byte)0x05E, (byte)0x05F,
      /* 20 - 23 */ (byte)0x060, (byte)0x061, (byte)0x0E2, (byte)0x0E3,
      /* 24 - 27 */ (byte)0x0E4, (byte)0x0E5, (byte)0x0E6, (byte)0x0E7,
      /* 28 - 2b */ (byte)0x0E8, (byte)0x0E9, (byte)0x06A, (byte)0x06B,
      /* 2c - 2f */ (byte)0x06C, (byte)0x06D, (byte)0x06E, (byte)0x06F,
      /* 30 - 33 */ (byte)0x0F0, (byte)0x0F1, (byte)0x0F2, (byte)0x0F3,
      /* 34 - 37 */ (byte)0x0F4, (byte)0x0F5, (byte)0x0F6, (byte)0x0F7,
      /* 38 - 3b */ (byte)0x0F8, (byte)0x0F9, (byte)0x07A, (byte)0x07B,
      /* 3c - 3f */ (byte)0x07C, (byte)0x07D, (byte)0x07E, (byte)0x07F,
    };

  static byte[] address(int location) {
    if (location > 0x0fff) {
      return new byte[] { (byte) ((location >> 8) & 0x03f),
                          (byte) ( location       & 0x0ff) };
    }
    else {
      return new byte[] { twelve[ (location >> 6) & 0x03f ],
                          twelve[ (location     ) & 0x03f ] };
    }
  }

  void sendHeader() throws IOException {
    urlc.writeBytes( MH.S3270_DATA, 0x00, 0x00,
                     ((responseSequence >> 8) & 0x0ff),
                     ((responseSequence ++  ) & 0x0ff) );
  }

  void sendTrailer() throws IOException {
    urlc.writeBytes( IAC, EOR );
  }


  public void sendModified(Text fields) throws IOException {
    int b;
    ByteBuffer buf = fields.text();
    for (int i = 0; i < fields.count(); i++) {
      if (fields.isModified(i)) {
        // SBA,address,text
        urlc.writeByte(E.SBA);
        urlc.writeBytes(address(fields.start(i)));
        int limit = fields.end(i);
        for (int j = fields.start(i); j < limit; j++) {
          if ((b = buf.get(j)) != 0) urlc.writeByte(b);
        }
      }
    }
  }

  public void processDataStream(int command, ByteBuffer buf) {
    if (command == E.WSF) {
      try {
        sendHeader();
        urlc.writeByte(AID.STRUCTURED_FIELD);
        urlc.writeBytes(Device.QueryReply());
        sendTrailer();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {
      System.out.println("ignore Data Stream: "+(buf.get(0) & 0x0ff)+' '+command);
    }
  }

  public String getPrimaryLUName(ByteBuffer buf) {
    int co = buf.get(26) & 0x00f;
    int p = 26+1+co;
    return LUName(buf,p);
  }

  public String getSecondaryLUName(ByteBuffer buf) {
    int co = buf.get(26) & 0x00f;
    int p = 26+1+co;
    int plu = buf.get(p) & 0x0ff;
    int d = p+1+plu;
    int udf = buf.get(d) & 0x0ff;
    int r = d+1+udf;
    int urcf = buf.get(r) & 0x0ff;
    int s = r+1+urcf;
    return LUName(buf,s);
  }

  String LUName(ByteBuffer buf, int offset) {
    int lu = buf.get(offset) & 0x0ff;
    byte[] b = new byte[lu];
    buf.position(offset+1);
    buf.get(b);
    try { return new String(b,"Cp037"); }
      catch (Exception e) {} // TODO: make a message
    return null;
  }

}