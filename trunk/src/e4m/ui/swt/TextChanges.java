package e4m.ui.swt;

import java.util.Iterator;

import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;

import e4m.util.LIFO;

class TextChanges {

  LIFO<TextChangeListener> listeners = new LIFO<TextChangeListener>();
  
  void addListener(TextChangeListener listener) {
    listeners.put(listener);
  }

  void removeListener(TextChangeListener listener) {
    for (Iterator<TextChangeListener> i = listeners.iterator();;) {
      if (i.next() == listener) {
        i.remove();
        return;
      }
    }
  }

  void textChanging(StyledTextContent source,
                    int start, String newText,
                    int replaceCharCount, int replaceLineCount,
                    int insertCharCount, int insertLineCount ) {
    TextChangingEvent event = new TextChangingEvent(source);
    event.start = start;
    event.newText = newText;
    event.replaceCharCount = replaceCharCount;
    event.replaceLineCount = replaceLineCount;
    event.newCharCount = insertCharCount;
    event.newLineCount = insertLineCount;
    for (TextChangeListener tcl : listeners)
      tcl.textChanging(event);
  }
  
  void textChanged(StyledTextContent source) {
    TextChangedEvent event = new TextChangedEvent(source);
    for (TextChangeListener tcl : listeners)
      tcl.textChanged(event);
  }
  
  void textSet(StyledTextContent source) {
    TextChangedEvent event = new TextChangedEvent(source);
    for (TextChangeListener tcl : listeners)
      tcl.textSet(event);
  }   
  
}
