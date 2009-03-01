package e4m.ui.html;

import java.io.OutputStream;
import java.nio.CharBuffer;

import e4m.net.tn3270.datastream.Text;
import e4m.ui.Codec;
import e4m.ui.SimpleGrid;

public class Html extends SimpleGrid {

  public Html(int rows, int columns) {
    super(rows, columns);
    grid = CharBuffer.wrap(buf);
  }
  
  Codec codec;
  CharBuffer grid;
  
  public void setCodec(Codec codec) {
    this.codec = codec;
  }

  public void format(CharSequence query, OutputStream out) {
    // TODO:
  }
  
  public CharSequence parse(Text fields) {
    codec.decode(fields,grid);
    StringBuilder sbuf = new StringBuilder();
    parse(fields,sbuf);
    return sbuf;
  }
  
  void parse(Text f, StringBuilder b) {
    b.append("<pre>");

    int row, col;
    int start=0, end=0;
    int edge = width();
    
    for (int i = 0; i < f.count(); i++) {
      if (f.length(i) < 1) continue;

      start = f.start(i);
      
      while (end < start) {
        if (end >= edge) {
          edge += width();
          b.append("<br>");
        }
        b.append(fill);
        end++;
      }
      
      end = f.end(i);
      row = rowAt(start);
      col = columnAt(start);
      
      if (f.isEditable(i)) {
        /*
            <input id=[r].[c]   value="[text]"   size=[n] maxlength=[n] >
        or
            <input id=[r].[c]   value="[text__]" size=[n] maxlength=[n] n=1><br>
            <input id=[r].[c].1 value="[_text_]" size=[n] maxlength=[n] n=2><br>
            <input id=[r].[c].2 value="[__text]" size=[n] maxlength=[n] >
        */
        
        int seq = 0;
        while (start < end) {
          
          b.append("<input")
           .append( " id=")
           .append(      row)
           .append(      '.')
           .append(      col);
          if (seq > 0) {
               b.append( '.')
                .append( seq);
          }
          
          b.append(" value=\"");
         
          int size = start;
          while (start < end) {
            if (start >= edge) {
              edge += width();
              seq++;
              break;
            }
            escape(b,charAt(start++));
          }  
          size = start - size;
          
          b.append('"')
           .append( " size=") 
           .append(        size)
           .append( " maxlength=")
           .append(             size);

          if (start < end) {
            b.append(" n=")
             .append(    seq)
             .append("><br>");
          }
        }
        b.append('>');  
      } 
      
      else { // f.isImmutable()
        /*
           <s style="...">[text]</s>
        or
           <s style="...">[text__]<br>
                          [_text_]<br>
                          [__text]</s>
        */
        
        b.append("<s" )
         .append( " id=")
         .append(      row)
         .append(      '.')
         .append(      col); 
                     styles(b,f,i);
        b.append(  '>');
     
        while (start < end) {
          if (start >= edge) {
            edge += width();
            b.append("<br>");
          }
          escape(b,charAt(start++));
        }
        
        b.append("</s>");
      }
        
    } // for ( f.count() )
        
    b.append("</pre>");
  }
  
  void escape(StringBuilder b, int c) {
    switch (c) {
      default:
        if (0x20 <= c && c <= 0x7e) {
          b.append((char)c);
          break;
        }
        // FALL-THROUGH
      case '&':
      case '<':
      case '>':
      case '"':
        b.append("&#");
        b.append(c);
        break;

      case 0:
        b.append(' ');
        break;         
    }
  }
  
  void styles(StringBuilder buf, Text f, int i) {
    StringBuilder s = new StringBuilder();

    int fg = f.getForeground(i);
    if (fg > 0) s.append("color:")
                 .append(color[fg & 0x0f])
                 .append(';');

    int bg = f.getBackground(i);
    if (bg > 0) s.append("background-color:")
                  .append(color[fg & 0x0f])
                  .append(';');

    if (s.length() > 0) {
      buf.append(" style=\"")
         .append(          s)
         .append(        '"');  
    }
  }
  
  public char fill = ' ';
  
  public CharSequence stylesheet() {
    return
      "pre{"                             +
           "background-color:black;"     +
           "color:green;"                +
           "font-family:monospace;"      +
           "font-size:8pt;"              +
           "}"                           +
      "input{"                           +
           "background-color:#141414;"   +
           "color:lime;"                 +
           "font-family:monospace;"      +
           "font-size:8pt;"              +
           "border-width:0;"             +
           "border-style:none;"          +
           "margin:0 0 0 0;"             +
           "}"                           +
      "s{"                               +
           "text-decoration:none;"       +
           "}"                           ;      
  }
  
  static String[] color = { //     3270 color      HTML color

    "#000000",  //  0  BACKGROUND      black
    "#0000ff",  //  1  BLUE            blue
    "#ff0000",  //  2  RED             red
    "#ff00ff",  //  3  PINK            fuchsia
    "#00ff00",  //  4  GREEN           lime
    "#00ffff",  //  5  TURQUOISE       aqua
    "#ffff00",  //  6  YELLOW          yellow
    "#ffffff",  //  7  FOREGROUND      white
    "#c0c0c0",  //  8  BLACK           silver
    "#000080",  //  9  DEEP_BLUE       navy
    "#800000",  // 10  ORANGE          maroon
    "#800080",  // 11  PURPLE          purple
    "#008000",  // 12  PALE_GREEN      green
    "#008080",  // 13  PALE_TURQUOISE  teal
    "#808000",  // 14  GREY            olive
    "#808080"   // 15  WHITE           gray
  };

}


//System.out.println("z: "+location+' '+start+' '+end);
//while (start < location) {
//if (edge-- < 1) {
//  edge += columns;
//  b.append("<br>");
//}
//b.append(fill);
//start++;
//}

//ByteBuffer in = f.text();
//start = in.position();
//end = in.limit();
//in.position(location);
//in.limit(f.end(i));
//CharBuffer c = decoder.decode(in);
//in.position(start);
//in.limit(end);

//if ( ! c.hasRemaining()) continue;
