package e4m.ui.swt;

import org.eclipse.swt.SWT;

import e4m.ref.CGA;

class ColorMap {
  
  static int color(int index) {
    switch (index) {
      default: return -1;
      
      case CGA.BACKGROUND:     return SWT.COLOR_BLACK;
      case CGA.BLUE:           return SWT.COLOR_BLUE;
      case CGA.RED:            return SWT.COLOR_RED;
      case CGA.PINK:           return SWT.COLOR_MAGENTA;
      case CGA.GREEN:          return SWT.COLOR_GREEN;
      case CGA.TURQUOISE:      return SWT.COLOR_CYAN;
      case CGA.YELLOW:         return SWT.COLOR_YELLOW;
      case CGA.FOREGROUND:     return SWT.COLOR_WHITE;
      
      case CGA.BLACK:          return SWT.COLOR_DARK_GRAY;        
      case CGA.DEEP_BLUE:      return SWT.COLOR_DARK_BLUE;      
      case CGA.ORANGE:         return SWT.COLOR_DARK_RED;
      case CGA.PURPLE:         return SWT.COLOR_DARK_MAGENTA;
      case CGA.PALE_GREEN:     return SWT.COLOR_DARK_GREEN;
      case CGA.PALE_TURQUOISE: return SWT.COLOR_DARK_CYAN;      
      case CGA.GREY:           return SWT.COLOR_DARK_YELLOW;
      case CGA.WHITE:          return SWT.COLOR_GRAY;
    }
  }
}
