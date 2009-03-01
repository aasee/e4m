package e4m.ui.ohio;

import e4m.net.tn3270.datastream.Text;
import e4m.ui.SimpleCodec;
import e4m.ui.SimpleForm;
import e4m.util.QuickSearch;

import org.ohio.OhioField;
import org.ohio.OhioFields;
import org.ohio.OhioPosition;
import static org.ohio.Ohio.*;

public class Fields implements OhioFields {

  Text text;
  SimpleForm form;
  Screen screen;
  QuickSearch search;
  int cursor;

  Fields(Screen screen) {
    this.screen = screen;
  }

  void setSize(int rows, int columns) {
    this.form = new SimpleForm(rows,columns);
  }

  void setCodec(String charsetName) {
    form.setCodec(new SimpleCodec(charsetName));
  }

  int getRows() { return form.height(); }
  int getColumns() { return form.width(); }

  String getString() { return form.toString(); }

  void setString(String text, OhioPosition location) {
      // TODO:
  }

  OhioPosition findString(String targetString, OhioPosition startPos, int length, Direction dir, boolean ignoreCase) {
      return null; // TODO:
  }

  byte[] getData(OhioPosition start, OhioPosition end, Plane plane) {
      return null; // TODO:
  }

  OhioPosition getCursor() { return position(cursor); }

  boolean atInput(boolean numeric) {
    return false; // TODO: return field state at cursor offset
                  // test for modifiable and numeric
  }

  public void refresh() {
    // TODO: get new Viewport
    //       update Fields
  }

  public int count() { return text.count(); }

  public OhioField findByString(String targetString, OhioPosition startPos, int length, Direction dir, boolean ignoreCase) {
    return null; // TODO:
  }

  public OhioField findByPosition(OhioPosition targetPosition) {
    return null; // TODO:
  }

  OhioPosition position(int offset) {
    return position(form.rowAt(offset),form.columnAt(offset));
  }

  OhioPosition position(final int r, final int c) {
    return new OhioPosition() {
      int row=r, column=c;
      public int row() { return row; }
      public int column() { return column; }
    };
  }

  int offset(OhioPosition position) {
    return form.indexAt(position.row(),position.column());
  }

  public OhioField getItem(int fieldIndex) {
    return field(fieldIndex);  // TODO; adjust fieldIndex
  }

  OhioField field(final int i) {
    return new OhioField() {
      int index = i;

      public OhioPosition getStart() { return position(text.start(index)); }
      public OhioPosition getEnd() { return position(text.end(index)); }
      public int getLength() { return text.length(index); }

      public boolean isModified() { return text.isModified(index); }
      public boolean isProtected() { return ! text.isEditable(index); }
      public boolean isNumeric() { return text.isNumeric(index); }
      public boolean isHighIntensity() { return text.isHighlighted(index); }
      public boolean isPenSelectable() { return text.isSelectable(index); }
      public boolean isHidden() { return text.isHidden(index); }

      public String getString() { return null; }                // TODO:
      public void setString(String text) {}                     // TODO:
      public int getAttribute() { return -1; }                  // TODO:
      public byte[] getData(Plane targetPlane) { return null; } // TODO:
      
    };
  }

  int find(char[] needle, char[] haystack, int start, int end) {
      return -1;
  }

}
