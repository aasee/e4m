package e4m.ui;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.BitSet;

import e4m.net.tn3270.datastream.Text;

public class SimpleForm extends SimpleGrid {
  
  public SimpleForm(int rows, int columns) {
    super(rows, columns);
    this.grid = CharBuffer.wrap(buf);
  }

  protected CharBuffer grid;
  protected Text field;
  protected BitSet mdt;
  
  boolean hasEditField;
  int currentEdit;
  
  public void setFields(Text fields) {
    this.field = fields;
    this.mdt = new BitSet(fields.count());
    currentEdit = -1;
    findEditable();
  }
  
  void findEditable() {
    for (int i = 0; i < field.count(); i++) {
      if (field.isEditable(i)) {
        hasEditField = true;
        return;
      }
    }
    hasEditField = false;
  }
  
  public boolean editFieldAt(int offset) {
    if (hasEditField) {
      int i = field.index(offset);
      if (i > -1 && field.isEditable(i)) {
        currentEdit = i;
        return true;
      }
    }  
    return false;
  }
  
  public void clearModifiedTags() {
    mdt.clear();
  }
  
  public void currentEditFieldModified() {
    mdt.set(currentEdit);
  }
  
  public int currentEditFieldStart() {
    return field.start(currentEdit);
  }
  
  public int currentEditFieldEnd() {
    return field.end(currentEdit);
  }
  
  public int nextEditFieldStart(int offset) {
    if (hasEditField) {
      int i = field.index(offset);
      if (i > -1) {
        if (field.isEditable(i)) i++;
        for (;;) {
          if (i >= field.count()) i = 0;
          if (field.isEditable(i)) break;
          i++;
        }
        currentEdit = i;
        return currentEditFieldStart();
      }
    }  
    return offset;
  }
  
  public int previousEditFieldStart(int offset) {
    if (hasEditField) {
      int i = field.index(offset);
      if (i > -1) {
        if (field.isEditable(i)) i--;
        for (;;) {
          if (i < 0) i = field.count() - 1;
          if (field.isEditable(i)) break;
          i--;
        }
        currentEdit = i;
        return currentEditFieldStart();
      }
    }  
    return offset;
  }

  protected Codec codec;
  
  public void setCodec(Codec codec) {
    this.codec = codec;
  }
  
  public void layoutFields() {
    codec.decode(field,grid);
    grid.position(0);
    grid.limit(grid.capacity());
  }
  
  public void syncFields() {
    ByteBuffer out = field.text();
    for (int i = 0; i < field.count(); i++) {
      if (mdt.get(i)) {
        if (codec.encode( field.getCharsetId(i), grid, out,
                          field.start(i), field.end(i) )) {
          field.setModified(i,true);
          continue;
        } 
        // log encode error
      }  
    }
    out.position(0);
    out.limit(out.capacity());
  }
  
}
