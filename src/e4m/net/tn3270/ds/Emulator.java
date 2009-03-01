package e4m.net.tn3270.ds;

class Emulator {

  Planar planar;  // set by Partition.class
  
  boolean resetPartition;
  boolean resetPrinter;
  boolean startPrinter;
  boolean soundAlarm;
  boolean restoreKeyboard;
  boolean resetModifiedDataTags;

  void Write_Control(int wcc) {
    resetPartition        = ( wcc & 0x040 ) != 0;
    resetPrinter          = ( wcc & 0x030 ) != 0;
    startPrinter          = ( wcc & 0x008 ) != 0;
    soundAlarm            = ( wcc & 0x004 ) != 0;
    restoreKeyboard       = ( wcc & 0x002 ) != 0;
    resetModifiedDataTags = ( wcc & 0x001 ) != 0;
  }

  void Write(int wcc) {
    Write_Control(wcc);  
  }
  
  void Erase_Write(int wcc) {
    /*  
     The EW command does the following:
     * Sets the implicit partition size to the default size, if in implicit state.
     * Resets a Program Check Indication, if one exists.
    =* Erases the character buffer by writing null characters into all buffer locations.
     * Sets all the associated character attributes and extended field attributes to their default value (X'00').
     * Erases all field validation attributes.
     * Sets the current cursor position to 0. If directed to a partition, autoscroll is performed, if necessary, to position the window at offset (0,0).
     * If bit 1 of the WCC is set to B'1', EW does the following:
         o Resets the inbound reply mode to Field.
         o Resets to implicit partition state, if currently in explicit partitioned state. It destroys all partitions, creates implicit partition 0 with default screen size, and sets inbound PID to 0 and INOP to Read Modified. 
     * Provides an acknowledgment of any outstanding read or enter if the keyboard restore bit in the WCC is set to B'1'.
     * Provides a negative trigger reply.
     * Performs a write operation.   
    */
    // TODO: Do other EraseWrite Actions
    planar.reset();
    Write(wcc);    
  }

  void Erase_Write_Alternate(int wcc) {
    throw new UnsupportedOperationException("command: Erase Write Alternate");
    // TODO: Do other EraseWriteAlternate Actions
    // EraseWrite(wcc);    
  }

  void Erase_All_Unprotected() {
    throw new UnsupportedOperationException("command: Erase All Unprotected");
  }
  
  void Read_Modified() {
    throw new UnsupportedOperationException("command: Read Modified");
  }  

  void Read_Modified_All() {
    throw new UnsupportedOperationException("command: Read Modified All");
  }  

  void Read_Buffer() {
    throw new UnsupportedOperationException("command: Read Buffer");
  }  

  void Set_Buffer_Address(int address) {
    planar.position(address);
  }

  void Start_Field(int attribute) {
    planar.markField(attribute & 0x03d);
    planar.putAttribute(Attribute.occupied);
    planar.next();
  }

  void Start_Field_Extended(byte[] tvp, int offset, int length) {
    int bits = 0;    
    int i = offset;
    int limit = offset + length;
 
    while (i < limit) {
      int type = tvp[i++] & 0x0ff;
      int mark = tvp[i++] & 0x0ff;
      
      switch (type) {
        default: throw new IllegalArgumentException("attribute type: "+toHex(type));
  
        case 0x0c0: planar.markField(mark & 0x03d);
                    bits = Attribute.setOccupied(bits,true);             break;
                    
        case 0x0c1: bits = Attribute.setFieldValidation(bits,mark);      break;
        case 0x0c2: bits = Attribute.setFieldOutlining(bits,mark);       break;
      
        case 0x041: bits = Attribute.setExtendedHighlighting(bits,mark); break;
        case 0x042: bits = Attribute.setForegroundColor(bits,mark);      break;
        case 0x045: bits = Attribute.setBackgroundColor(bits,mark);      break;
        case 0x043: bits = Attribute.setCharacterSetId(bits,mark);       break;
        case 0x046: bits = Attribute.setFieldTransparency(bits,mark);    break;
      }  
    }
    planar.putAttribute(bits);
    
    if (Attribute.occupied(bits)) {
      planar.next();
    }
  }

  void Modify_Field(byte[] tvp, int offset, int length) {
    throw new UnsupportedOperationException("order: Modify Field");
  }

  void Set_Attribute(byte[] tvp, int offset, int length) {
    int bits = 0;    
    int i = offset;
    int limit = offset + length;
 
    while (i < limit) {
      int type = tvp[i++] & 0x0ff;
      int mark = tvp[i++] & 0x0ff;
      
      switch (type) {
        default: throw new IllegalArgumentException("attribute type: "+toHex(type));
  
        case 0x000: break;  // TODO: Reset all character attributes
      
        case 0x041: bits = Attribute.setExtendedHighlighting(bits,mark); break;
        case 0x042: bits = Attribute.setForegroundColor(bits,mark);      break;
        case 0x045: bits = Attribute.setBackgroundColor(bits,mark);      break;
        case 0x043: bits = Attribute.setCharacterSetId(bits,mark);       break;
        case 0x046: bits = Attribute.setFieldTransparency(bits,mark);    break;
      }  
    }
    planar.putAttribute(bits);
  }
  
  void Erase_Unprotected_To_Address(int address) {
    throw new UnsupportedOperationException("order: Erase Unprotected to Address");
  }

  void Graphic_Escape(int code) {
    // TODO: mark as Graphic Escape byte or map 'code' to another byte
    Set_Character(0x079); // use 'grave' instead
  }
  
  void Set_Character(int code) {
    planar.putByte(code);
    planar.next();
  }

  void Repeat_To_Address(int address, int fill) {
    if (planar.position() > address) {
      Repeat_To_Address(planar.length(),fill);
    }
    while (planar.position() < address) {
      planar.putByte(fill);
      planar.next();
      if (planar.position() < 1) break;  // if position wrapped
    }
  }

  void Insert_Cursor() {
    planar.markCursor();
  }

  void Program_Tab() {
    throw new UnsupportedOperationException("order: Program Tab");
  }

  void Write_Structured_Field(byte[] sf, int offset, int length) {
    switch (sf[offset] & 0x0ff) { // SFID
      default:
        throw new UnsupportedOperationException(
            "structured field: "+toHex(sf[0])+','+toHex(sf[1]));
    
      case 0x040: Outbound_3270DS(sf,offset,length); break;
      case 0x001: Read_Partition(sf,offset,length);  break;      
    }
  }
  
  void Outbound_3270DS(byte[] sf, int offset, int length) {
    // System.err.println("Outbound_3270DS: "+offset+' '+length); // TODO: no-op
  }
  
  void Read_Partition(byte[] sf, int offset, int length) {
    // System.err.println("Read_Partition: "+offset+' '+length); // TODO: no-op
  }

  static String toHex(int b) {
    return Integer.toHexString((b&255)+256).substring(1);
  }

}
