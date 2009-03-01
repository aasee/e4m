package e4m.ui.swt;

import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.events.VerifyEvent;

import e4m.net.tn3270.datastream.Text;
import e4m.ui.SimpleForm;

class TextContent extends SimpleForm implements StyledTextContent {

  TextChanges reporter;
  
  TextContent(int rows, int columns) {
    super(rows,columns);
    this.reporter = new TextChanges();
  }
  
  void updateFields(Text fields) {
    setFields(fields);
    layoutFields();
  }
  
  Text getFields() {
    syncFields();
    return this.field;
  }
  
  public void addTextChangeListener(TextChangeListener listener) {
    reporter.addListener(listener);
  }

  public void removeTextChangeListener(TextChangeListener listener) {
    reporter.removeListener(listener);
  }

  public String getLineDelimiter() { 
    return "\u0000";
  }

  public int getCharCount() {
    return length();
  }
  
  public int getLineCount() {
    return height();
  }
  
  public String getLine(int lineIndex) {
    char[] ch = copy(getOffsetAtLine(lineIndex), width());
    for (int i = 0; i < ch.length; i++) {
      if (ch[i] < ' ') ch[i] = ' ';
    }  
    return new String(ch);
  }

  public int getLineAtOffset(int offset) {
    return rowAt(offset);
  }
  
  public int getOffsetAtLine(int lineIndex) {
    return indexAt(lineIndex,0);
  }

  public String getTextRange(int start, int length) {
    return subSequence(start,start+length).toString();
  }

  /* textChanging(
   *   start             Start offset of the text that is going to be replaced
   *   newText           Text that is going to be inserted or empty string if no text will be inserted
   *   replaceCharCount  Length of text that is going to be replaced
   *   replaceLineCount  Number of lines that are going to be replaced
   *   newCharCount      Length of text that is going to be inserted
   *   newLineCount      Number of new lines that are going to be inserted
   */

  public void replaceTextRange(int start, int replaceLength, String text) {
    reporter.textChanging(this, start, text, replaceLength,0, text.length(),0 );
    replaceText(start,replaceLength,text);
    reporter.textChanged(this);
  }

  public void setText(String text) {
    replaceText(0,length(),text);
    reporter.textSet(this);
  }
  
  void replaceText(int start, int length, CharSequence text) {
    if (editFieldAt(start)) {
      replace( start, currentEditFieldEnd(), length, text, true );
      currentEditFieldModified();
    }
  }
  
  void verifyText(VerifyEvent e) {
    e.doit = editFieldAt(e.start);
    if (e.doit) {
      int range_end = currentEditFieldEnd();
      int e_length = e.end - e.start;
      if (e_length == 0) {
        e.doit = insert( e.start, range_end, e.text, false );
      } else {  
        if (e.text.length() == 0) {
          e.doit = delete( e.start, range_end, e_length, false );
        } else {  
          e.doit = replace( e.start, range_end, e_length, e.text, false );
        }
      }
      currentEditFieldModified();
    }  
  }
  
}