package e4m.net.tn3270;

import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import e4m.io.ByteCollector;
import e4m.net.tn3270.datastream.Viewport;
import e4m.net.tn3270.datastream.Text;
import e4m.net.tn3270.ds.Partition;

import static e4m.ref.GA23_0059.*;

public class Tn3270Content extends ContentHandler {

  @Override
  public Object getContent(URLConnection urlc) throws IOException {
    return getPacket((Tn3270Connection)urlc);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getContent(URLConnection urlc, Class[] classes) throws IOException {
    if (classes != null && classes.length == 1) {
      if (Viewport.class.equals(classes[0]))
        return getContent(urlc);
      if (ByteBuffer.class.equals(classes[0]))
        return getByteBuffer((Tn3270Connection)urlc);
      if (Partition.class.equals(classes[0]))
        return ((Tn3270Connection)urlc).partition;
    }
    return super.getContent(urlc,classes);
  }

  ByteBuffer getByteBuffer(Tn3270Connection c) throws IOException {
    InputStream in = c.getInputStream();
    ByteCollector out = new ByteCollector();
    int b; while ((b = in.read()) > -1) out.write(b);
    ByteBuffer buf = out.toByteBuffer();
    return buf;
  }

  Viewport getPacket(Tn3270Connection c) throws IOException {
    View s = new View();
    s.datastream = getByteBuffer(c);
    c.partition.write(s.datastream);

    s.dimension = c.partition.getSize();
    Partition.State r = c.partition.getState();
    s.header = r.header;
    s.command = r.command;
    if (hasFields(s.command)) {
      s.cursor = r.cursor;
      s.resetModifiedDataTags = r.resetModifiedDataTags;
      s.restoreKeyboard = r.restoreKeyboard;
      s.soundAlarm = r.soundAlarm;
      s.fields = c.partition.getView();
    }

    return s;
  }

  class View implements Viewport {
    ByteBuffer datastream;
    byte[] header;
    int command;
    int cursor;
    boolean restoreKeyboard;
    boolean resetModifiedDataTags;
    boolean soundAlarm;
    int[] dimension;
    Text fields;

    public ByteBuffer datastream() { return datastream; }
    public byte[] header() { return header; }
    public int command() { return command; }
    public int cursor() { return cursor; }
    public boolean restoreKeyboard() { return restoreKeyboard; }
    public boolean resetModifiedDataTags() { return resetModifiedDataTags; }
    public boolean soundAlarm() { return soundAlarm; }
    public int rows() { return dimension[0]; }
    public int columns() { return dimension[1]; }
    public Text fields() { return fields; }
  }

  static boolean hasFields(int cmd) {
    switch (cmd) {
      default: return false;

      case A.Wr:  case E.Wr:  case S.Wr: // Write
      case A.EW:  case E.EW:  case S.EW: // Erase Write
      case A.EWA: case E.EWA:            // Erase Write Alternate
                                         return true;
    }
  }

}
