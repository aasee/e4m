package e4m.net.tn3270.ds;

import java.nio.ByteBuffer;
import static e4m.ref.GA23_0059.*;
import static e4m.ref.RFC_2355.*;

class Parser {

  ByteBuffer in;
  Emulator out;
  byte[] header = new byte[MH.LENGTH];
  int command;

  void parse(ByteBuffer in, Emulator out) {
    this.in = in;
    this.out = out;
    in.get(header);
    if ( header[MH.DATA_TYPE] == MH.S3270_DATA ||
         header[MH.DATA_TYPE] == MH.SSCP_LU_DATA ) {
      DataStream_Command();
    } else {
      command = -1;
    }
  } 

  int getAddress() {
    int hi = in.get(), lo = in.get();
    return ((hi & 0x0c0) > 0) ? ((hi & 0x03f) << 6) | (lo & 0x03f)
                              : ((hi & 0x03f) << 8) | (lo & 0x0ff);
  }

  void skipBytes(int n) {
    in.position( in.position() + n );
  }

  int nextByte() {
    return in.get() & 0x0ff;
  }

  void DataStream_Command() {
    command = nextByte();
    switch (command) {
      default:
        throw new UnsupportedOperationException("command: "+toHex(command));

      case A.Wr:  case E.Wr:  case S.Wr:  Write();                  break;
      case A.EW:  case E.EW:  case S.EW:  Erase_Write();            break;
      case A.EWA: case E.EWA:             Erase_Write_Alternate();  break;
      case A.EAU: case E.EAU:             Erase_All_Unprotected();  break;
      case A.RB:  case E.RB:              Read_Buffer();            break;
      case A.RM:  case E.RM:              Read_Modified();          break;
      case A.RMA: case E.RMA:             Read_Modified_All();      break;
                  case E.WSF:             Write_Structured_Field(); break;
    }
  }
  
  void Write() {
    int wcc = nextByte();
    out.Write(wcc);
    DataStream_Orders(); 
  }
  
  void Erase_Write() {
    int wcc = nextByte();
    out.Erase_Write(wcc);
    DataStream_Orders(); 
  }
  
  void Erase_Write_Alternate() {
    int wcc = nextByte();
    out.Erase_Write_Alternate(wcc);
    DataStream_Orders();
  }

  void Erase_All_Unprotected() {
    out.Erase_All_Unprotected();
  }
  
  void Read_Modified() {
    out.Read_Modified();
  }
  
  void Read_Modified_All() {
    out.Read_Modified_All();
  }
  
  void Read_Buffer() {
    out.Read_Buffer();
  }
  
  void Write_Structured_Field() {
    while (in.hasRemaining()) {
      int length = in.getShort() - 2;
      out.Write_Structured_Field( in.array(), in.position(), length );
      skipBytes(length);
    }
  }

  void DataStream_Orders() {
    while (in.hasRemaining()) {
      int b = nextByte();
      switch (b) {

        case E.SBA:  Set_Buffer_Address();           break;
        case E.SF:   Start_Field();                  break;
        case E.SFE:  Start_Field_Extended();         break;
        case E.MF:   Modify_Field();                 break;
        case E.SA:   Set_Attribute();                break;
        case E.RA:   Repeat_To_Address();            break;
        case E.EUA:  Erase_Unprotected_To_Address(); break;
        case E.GE:   Graphic_Escape();               break;

        case E.IC:   out.Insert_Cursor();            break;
        case E.PT:   out.Program_Tab();              break;

        default:     out.Set_Character(b);           break;
      }
    }  
  }

  void Set_Buffer_Address() {
    int address = getAddress();
    out.Set_Buffer_Address(address);
  }
  
  void Start_Field() {
    int attribute = nextByte();
    out.Start_Field(attribute);
  }

  void Start_Field_Extended() {
    int count = 2 * nextByte();
    out.Start_Field_Extended( in.array(), in.position(), count);
    skipBytes(count);
  }
  
  void Modify_Field() {
    out.Modify_Field( in.array(), in.position(), 2 );
    skipBytes(2);
  }
  
  void Set_Attribute() {
    out.Set_Attribute( in.array(), in.position(), 2 );
    skipBytes(2);
  }
  
  void Repeat_To_Address() {
    int address = getAddress();
    int code = nextByte();
    if (code == E.GE) { // GE - Graphics Escape
      code = nextByte();
    }
    out.Repeat_To_Address(address,code);
  }
  
  void Erase_Unprotected_To_Address() {
    int address = getAddress();
    out.Erase_Unprotected_To_Address(address);
  }
  
  void Graphic_Escape() {
    int code = nextByte();
    out.Graphic_Escape(code);
  }

  static String toHex(int b) {
    return "<"+Integer.toHexString((b&255)+256).substring(1)+'>';
  }

}
