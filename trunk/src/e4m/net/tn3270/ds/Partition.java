package e4m.net.tn3270.ds;

import java.nio.ByteBuffer;
import java.util.Arrays;

import e4m.net.tn3270.datastream.Text;

public class Partition {

  Emulator emulator;
  Parser parser;
  Planar planar;
  int[] dimension;

  public Partition() {
    this.emulator = new Emulator();
    this.parser = new Parser();
  }

  public void setSize(int rows, int columns) {
    this.planar = new Planar(rows,columns);
    this.dimension = new int[] {rows,columns};
  }

  public int[] getSize() {
    return dimension;
  }

  public void write(ByteBuffer in) {
    emulator.planar = planar;
    parser.parse(in,emulator);
  }

  public State getState() {
    State s = new State();
    s.header = parser.header;
    s.command = parser.command;
    s.cursor = planar.cursorPosition;
    s.restoreKeyboard = emulator.restoreKeyboard;
    s.soundAlarm = emulator.soundAlarm;
    s.resetModifiedDataTags = emulator.resetModifiedDataTags;
    return s;
  }

  public class State {
    public byte[] header;
    public int command;
    public int cursor;
    public boolean restoreKeyboard;
    public boolean soundAlarm;
    public boolean resetModifiedDataTags;
  }

  public ByteBuffer read(boolean modified) {
    return null;
  }

  public Text getView() {
    Object[] p = planar.getView();
    PresentationSpace v = new PresentationSpace();
    v.start = (short[]) p[0];
    v.attribute = (int[]) p[1];
    v.buf = ByteBuffer.wrap(planar.text);
    return v;
  }

  public class PresentationSpace implements Text {
  
    short[] start;
    int[] attribute;
    ByteBuffer buf;
  
    public ByteBuffer text() {
      return buf;
    }
  
    public int count() {
      return attribute.length;
    }

    public int prefix(int i) {
      return Attribute.occupied(extendedAttribute(i)) ? start[i] : -1;
    }

    public int start(int i) {
      return Attribute.occupied(extendedAttribute(i)) ? start[i] + 1 : start[i];
    }
    
    public int end(int i) {
      return start[i+1];
    }
  
    public int length(int i) {
      return end(i) - start(i);
    }
  
    public int index(int offset) {
      if (offset < start[0] || start[start.length - 1] <= offset) {
        return -1;
      }
      int index = Arrays.binarySearch(start, (short)offset);
      return (index < 0) ? -(index+2) : index;
    }

    public long attributes(int i) {
      long ea = extendedAttribute(i);
      int fa = fieldAttribute(i);
      return (ea << 8) | (fa & 0x0ff);
    }
    
    int extendedAttribute(int i) {
      return attribute[i];
    }
    
    int fieldAttribute(int i) {
      return buf.get( start[i] ) & 0x0ff;
    }
    
    public void setModified(int i, boolean mdt) {
      buf.put( start[i], (byte) Attribute.modifyTag(fieldAttribute(i),mdt) ); 
    } 
     
    public boolean isEditable(int i) {
      return Attribute.mutable(fieldAttribute(i));
    }
    public boolean isNumeric(int i) {
      return Attribute.numeric(fieldAttribute(i));
    }
    public boolean isSkipped(int i) {
      return Attribute.skipped(fieldAttribute(i));
    }
    public boolean isVisible(int i) {
      return Attribute.visible(fieldAttribute(i));
    }
    public boolean isBright(int i) {
      return Attribute.bright(fieldAttribute(i));
    }
    public boolean isHidden(int i) {
      return Attribute.hidden(fieldAttribute(i));
    }
    public boolean isModified(int i) {
      return Attribute.modified(fieldAttribute(i));
    }
    public boolean isSelectable(int i) {
      return Attribute.selectable(fieldAttribute(i));
    }
  
    public boolean isHighlighted(int i) {
      return Attribute.highlighted(extendedAttribute(i));
    }
    public boolean isBlinking(int i) {
      return Attribute.blinking(extendedAttribute(i));
    }
    public boolean isReversed(int i) {
      return Attribute.reversed(extendedAttribute(i));
    }
    public boolean isUnderscored(int i) {
      return Attribute.underscored(extendedAttribute(i));
    }
  
    public boolean isMandatoryFill(int i) {
      return Attribute.mandatoryFill(extendedAttribute(i));
    }
    public boolean isMandatoryEntry(int i) {
      return Attribute.mandatoryEntry(extendedAttribute(i));
    }
    public boolean isTrigger(int i) {
      return Attribute.trigger(extendedAttribute(i));
    }
  
    public boolean isXORed(int i) {
      return Attribute.XORed(extendedAttribute(i));
    }
    public boolean isORed(int i) {
      return Attribute.ORed(extendedAttribute(i));
    }
    public boolean isTransparent(int i) {
      return Attribute.transparent(extendedAttribute(i));
    }
  
    public int getForeground(int i) {
      return Attribute.getForegroundColor(extendedAttribute(i));
    }
    public int getBackground(int i) {
      return Attribute.getBackgroundColor(extendedAttribute(i));
    }
    
    public int getCharsetId(int i) {
      return Attribute.getCharacterSetId(extendedAttribute(i));
    }
  }

}
