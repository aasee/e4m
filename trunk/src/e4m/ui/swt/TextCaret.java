package e4m.ui.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

class TextCaret {

  TextCaret(Control widget, Color foreground, Color background) {
    this.display = widget.getDisplay();
    this.background = background;
    this.foreground = foreground;
    getFontMetrics(widget.getFont());
  }
  
  Display display;
  Color background, foreground;
  int leading, ascent, descent;
  int baseline;
  int height, width;
  
  void getFontMetrics(Font font) {
    GC gc = new GC(display);
    gc.setFont(font);
    FontMetrics fm = gc.getFontMetrics();
    leading = fm.getLeading();
    ascent = fm.getAscent();
    descent = fm.getDescent();
    baseline = leading + ascent;
    height = fm.getHeight();
    width = fm.getAverageCharWidth();
    gc.dispose();
  }

  Image createReadonlyCaretBitmap() {
    Image readonlyCaretBitmap = new Image(display, width, height);
    GC gc = new GC(readonlyCaretBitmap); 
    gc.setBackground(background);
    gc.fillRectangle(0,0,width,height);
    gc.setForeground(foreground);
    gc.drawLine(0,height-1,width,height-1);
    gc.dispose();
    return readonlyCaretBitmap;
  }
  
  Image createInsertCaretBitmap() {
    Image insertCaretBitmap = new Image(display, width, height);
    GC gc = new GC(insertCaretBitmap); 
    gc.setBackground(background);
    gc.fillRectangle(0,0,width,height);
    gc.setForeground(foreground);
    gc.drawLine(0,0,0,height);
    gc.dispose();
    return insertCaretBitmap;
  }
  
  Image createOverwriteCaretBitmap() {
    Image overwriteCaretBitmap = new Image(display, width, height);
    GC gc = new GC(overwriteCaretBitmap); 
//  gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
//  gc.fillRectangle(0,0,width,height);
    gc.setForeground(foreground);
    gc.fillRectangle(0,0,width-1,height-1);
    gc.dispose();
    return overwriteCaretBitmap; 
  }

}
