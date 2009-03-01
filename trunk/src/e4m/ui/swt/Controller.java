package e4m.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;

import e4m.net.Vty.AttentionListener;
import e4m.net.tn3270.datastream.Text;
import e4m.ui.Codec;

class Controller {
  
  StyledText widget;
  TextContent content;
  TextStyle defaultStyle;
  TextRanges styles;
  
  boolean numLock = Boolean.getBoolean("numlock.initial.state");

  void reset(boolean kb, boolean mdt) {
    if (kb) enableKeyboard();
  }

  StyledText getWidget() {
    return widget;
  }
  
  void setWidget(StyledText widget, Font defaultFont) {
    this.widget = widget;

    setDefaultStyle(
       new TextStyle( defaultFont,
                      widget.getDisplay().getSystemColor(SWT.COLOR_WHITE),
                      widget.getDisplay().getSystemColor(SWT.COLOR_BLACK) ));
    
    widget.setEditable(true);
    widget.addVerifyKeyListener(verifyKeyListener);
    widget.addModifyListener(modifyListener);
    widget.addMouseListener(mouseListener);
    widget.addTraverseListener(traverseListener);
    widget.addLineBackgroundListener(lineBackgroundListener);
    widget.addLineStyleListener(lineStyleListener);
    widget.addVerifyListener(verifyListener);
    widget.addDisposeListener(disposeListener);
  }

  DisposeListener disposeListener = new DisposeListener() {
    public void widgetDisposed(DisposeEvent e) {
      System.err.println("DisposeListener called.");
    }
  };
  
  void setSize(int rows, int columns) {
    styles = new TextRanges(rows,columns);
    content = new TextContent(rows,columns);
    widget.setContent(content);
    resize();
  }
  
  void resize() {
    if (content == null) return;
    Rectangle r = caretImage[OVERWRITE].getBounds();
    int y = content.getLineCount();
    widget.setSize( r.width * (content.getCharCount() / y), r.height * y );
  }  

  void setCodec(Codec codec) {
    content.setCodec(codec);
  }

  void updateFields(int command, int cursor, Text fields) {
    styles.createStyleRanges(fields,defaultStyle);
    content.updateFields(fields);
    widget.redraw();
    if (cursor < 0) 
      traverseInput(widget,true);
    else
      setCursor(cursor);
  }

  void setAttentionListener(AttentionListener listener) {
    this.attentionListener = listener; 
  }
  
  void setDefaultStyle(TextStyle style) {
    defaultStyle = style;
    widget.setFont(defaultStyle.font);
    createCaretImages();
    unsetCaretImage();
    resize();
  }

  void setCursor(int position) {
    setCaretImageAndOffset( position % widget.getCharCount() );
  }
  
  int getCursor() {
    return widget.getCaretOffset();
  }
  
  TraverseListener traverseListener = new TraverseListener() {
    public void keyTraversed(TraverseEvent e) {
      switch (e.detail) {
        case SWT.TRAVERSE_TAB_PREVIOUS: traverseInput(e,false); break;
        case SWT.TRAVERSE_TAB_NEXT: traverseInput(e,true); break;
      }  
    }
  };

  void traverseInput(TraverseEvent e, boolean forward) {
    traverseInput( (StyledText) e.getSource(), forward );
  }
  
  void traverseInput(StyledText s, boolean forward) {
    int offset = s.getCaretOffset();
    offset = forward ? content.nextEditFieldStart(offset)
                     : content.previousEditFieldStart(offset);     
    s.setCaretOffset(offset);
    setCaretImage(offset);
  }

  ModifyListener modifyListener = new ModifyListener() {
    public void modifyText(ModifyEvent e) {
      if (widget.getCaretOffset() >= content.currentEditFieldEnd()) {
        traverseInput(widget,true);
      }  
    }    
  };
  
  VerifyListener verifyListener = new VerifyListener() {
    public void verifyText(VerifyEvent e) {
      if (content != null)
        content.verifyText(e);
      else
        e.doit = true;
    }
  };  
  
  LineBackgroundListener lineBackgroundListener = new LineBackgroundListener() {
    public void lineGetBackground(LineBackgroundEvent e) {
      e.lineBackground = defaultStyle.background;
    }    
  };
  
  LineStyleListener lineStyleListener = new LineStyleListener() {
    public void lineGetStyle(LineStyleEvent e) {
      if (styles != null)
        styles.lineGetStyle(e);
      else
        e.styles = new StyleRange[] {new StyleRange()}; 
    }
  };

  MouseListener mouseListener = new MouseListener() {
    public void mouseUp(MouseEvent e) {
      setCaretImage( ((StyledText)(e.getSource())).getCaretOffset() );
    }
    public void mouseDoubleClick(MouseEvent e) {}
    public void mouseDown(MouseEvent e) {}
  };

  VerifyKeyListener verifyKeyListener = new VerifyKeyListener() {
    public void verifyKey(VerifyEvent e) {
      if (inputInhibited) {
        if (e.keyCode == SWT.ESC) {
          enableKeyboard();
        }
        e.doit = false;
      }
      else {
        if (attentionKey(e)) {
          e.doit = false;
        } else {
          e.doit = regularKey(e);
        }
      }  
    }
  };

  boolean regularKey(VerifyEvent e) {
    switch (e.keyCode) {
      default: return true;

      case SWT.ARROW_DOWN:  return arrowDown();
      case SWT.ARROW_LEFT:  return arrowLeft();
      case SWT.ARROW_RIGHT: return arrowRight();
      case SWT.ARROW_UP:    return arrowUp();

      case SWT.INSERT:   return insertKey();
      case SWT.NUM_LOCK: return numLock();
      
      case SWT.HOME: return homeKey();
      case SWT.END:  return endKey();
      case SWT.TAB:  return tabKey();
      
      case SWT.ESC: return enableKeyboard();
    }
  }

  KeyMap keymap = new KeyMap();
  AttentionListener attentionListener;
  
  boolean attentionKey(VerifyEvent e) {
    int aid = keymap.attention(e.keyCode);
    if (aid > 0) {
      if (attentionListener != null) {
        disableKeyboard();
        attentionListener.attention(aid,getCursor(),content.getFields());
        return true;
      }
    }
    return false;
  }

  boolean inputInhibited = false;
  
  void disableKeyboard() {
    inputInhibited = true;
  }
  
  boolean enableKeyboard() {
    inputInhibited = false;
    return false;
  }
  
  boolean homeKey() { return false; } // TODO:
  boolean endKey() { return false; } // TODO:
  boolean tabKey() { return false; } // TODO:

  boolean doIt(VerifyEvent e) {
    System.err.println("k: "+e.keyCode
                        +' '+Integer.toHexString(e.character)
                        +' '+Integer.toHexString(e.stateMask) );
    return true;
  }
  
  boolean numLock() {
    numLock = ! numLock;
    return false;
  }
  
  boolean insertKey() {
    toggleOverwrite();
    return true;
  }

  boolean arrowRight() {
    int offset = widget.getCaretOffset() + 1; 
    if ( offset < widget.getCharCount()) {
      setCaretImage(offset);
      return true;
    }  
    setCaretImageAndOffset(0);
    return false;
  }
  
  boolean arrowLeft() {
    int offset = widget.getCaretOffset();
    if ( offset > 0 ) {
      if ( offset % getLineWidth() > 0 ) {
        setCaretImage(offset - 1);
        return true;
      }
      offset -= 1;
    } else {
      offset = widget.getCharCount() - 1;
    }  
    setCaretImageAndOffset(offset);
    return false;
  }
  
  boolean arrowDown() {
    int width = getLineWidth();
    int offset = widget.getCaretOffset() + width;
    if (offset < widget.getCharCount()) {
      setCaretImage(offset);
      return true;
    }
    setCaretImageAndOffset(offset %= width);
    return false;  
  }
  
  boolean arrowUp() {
    int width = getLineWidth();
    int offset = widget.getCaretOffset();
    if (offset > width) {
      setCaretImage(offset - width);
      return true;
    }
    offset = (width > offset) ? widget.getCharCount() - (width-offset) : 0;
    setCaretImageAndOffset(offset);
    return false;
  }  

  void setCaretImageAndOffset(int offset) {
    widget.setCaretOffset(offset);
    setCaretImage(offset); 
  }

  int getLineWidth() {
    return widget.getCharCount() / widget.getLineCount();
  }
  
  
  Image[] caretImage;
  int caretIndex;
  
  final static int READONLY = 0;
  final static int INSERT = 1;
  final static int OVERWRITE = 2;
  
  void createCaretImages() {
    TextCaret cm = new TextCaret( widget, defaultStyle.foreground,
                                          defaultStyle.background );
    caretImage = new Image[3];
    caretImage[READONLY] = cm.createReadonlyCaretBitmap();
    caretImage[INSERT] = cm.createInsertCaretBitmap();
    caretImage[OVERWRITE] = cm.createOverwriteCaretBitmap();
  }
  
  void unsetCaretImage() {
    caretIndex = -1;
  }
  
  void setCaretImage(int offset) {
    int index = content.editFieldAt(offset) ?
                                ( overwrite ? OVERWRITE : INSERT ) : READONLY;
    if (caretIndex != index) {
      caretIndex = index;
      widget.getCaret().setImage(caretImage[caretIndex]); 
    }
  }

  boolean overwrite = false;       // insert/overwrite edit mode
  
  void toggleOverwrite() {
    overwrite = ( ! overwrite );   // toggle insert/overwrite mode
    setCaretImage(widget.getCaretOffset());
  }

}
