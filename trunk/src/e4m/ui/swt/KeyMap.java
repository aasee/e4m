package e4m.ui.swt;

import org.eclipse.swt.SWT;

import e4m.ref.AID;

class KeyMap {

  int attention(int keyCode) {
    switch (keyCode) {
      default: return -1;

      //   SWT.ESC:             -> RESET
    
      case SWT.F1:              return AID.PF1;          
      case SWT.F2:              return AID.PF2;          
      case SWT.F3:              return AID.PF3;          
      case SWT.F4:              return AID.PF4;          
      case SWT.F5:              return AID.PF5;          
      case SWT.F6:              return AID.PF6;          
      case SWT.F7:              return AID.PF7;          
      case SWT.F8:              return AID.PF8;          
      case SWT.F9:              return AID.PF9;          
      case SWT.F10:             return AID.PF10;         
      case SWT.F11:             return AID.PF11;         
      case SWT.F12:             return AID.PF12;
      
      //   SWT.F13:                
      //   SWT.F14:               
      //   SWT.F15:

      case SWT.PAGE_UP:         return AID.PA1;
      case SWT.PAGE_DOWN:       return AID.PA2;
      case SWT.SCROLL_LOCK:     return AID.PA3;
      case SWT.PAUSE:           return AID.CLEAR;
  
      case SWT.KEYPAD_0:        return AID.PF13;       
      case SWT.KEYPAD_1:        return AID.PF14;       
      case SWT.KEYPAD_2:        return AID.PF15;       
      case SWT.KEYPAD_3:        return AID.PF16;       
      case SWT.KEYPAD_4:        return AID.PF17;       
      case SWT.KEYPAD_5:        return AID.PF18;       
      case SWT.KEYPAD_6:        return AID.PF19;       
      case SWT.KEYPAD_7:        return AID.PF20;       
      case SWT.KEYPAD_8:        return AID.PF21;       
      case SWT.KEYPAD_9:        return AID.PF22;       
      case SWT.KEYPAD_DIVIDE:   return AID.PF23;
      case SWT.KEYPAD_MULTIPLY: return AID.PF24;
      
      //   SWT.KEYPAD_SUBTRACT:
      //   SWT.KEYPAD_ADD:    
      //   SWT.KEYPAD_DECIMAL: 
      //   SWT.KEYPAD_EQUAL:
      
      case SWT.KEYPAD_CR:       return AID.SYS_REQ; 

      case SWT.CR:              return AID.ENTER;
    
      //   SWT.HOME:            -> HOME
      //   SWT.END:             -> END
    }
  }
}
