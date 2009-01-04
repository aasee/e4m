package e4m.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

import e4m.net.tn3270.datastream.Viewport;

class TextRanges {

  StyleRange[][] lineStyles; // lineStyles[rows][columns]
  int height, width;

  TextRanges(int height, int width) {
    this.height = height;
    this.width = width;    
  }
  
  void createStyleRanges(Viewport fields, TextStyle defaultStyle) {
    lineStyles = new StyleRange[height][];
    createLineStyles(fields, defaultStyle);
  }

  void lineGetStyle(LineStyleEvent e) {
    e.styles = (lineStyles != null) ? lineStyles[ e.lineOffset / width ]
                                    : new StyleRange[] {new StyleRange()};
  }

  void createLineStyles(Viewport tags, TextStyle defaultStyle) {
    createLineStyles(lineStyles, height, width, tags, defaultStyle);
    
    // for (StyleRange[] list : lineStyles)
    //   for (StyleRange range : list)
    //     System.err.println("s: "+range);
  }

  static void createLineStyles(StyleRange[][] lines, int rows, int columns,
                               Viewport tags, TextStyle defaultStyle) {
    StyleRange item;
    List<StyleRange> list = new ArrayList<StyleRange>();

    int textOffset = 0; // current end of StyleRange'd text
    int textLimit = rows * columns; // end of StyleRange'd text

    int lineOffset = columns; // end of current line
    int lineIndex = 0; // lineStyles index

    int i = 0; // Tag[] index;
    StyleRange range = null;

    while (textOffset < textLimit) {
      if (i < tags.count()) {
        /* in tags[] range */
        if (range == null) {
          range = style(defaultStyle,tags,i++);
        }  
        if (textOffset < range.start) {
          /* textOffset < start, handle leading gap */
          item = new StyleRange(defaultStyle);
          item.start = textOffset;
          item.length = min(range.start, lineOffset) - item.start;
        }
        else {
          /* start <= textOffset, no leading gap */
          if (lineOffset < range.start + range.length) {
            /* start < lineOffset < end, split the range */
            item = new StyleRange(range);
            item.start = range.start;
            item.length = lineOffset - item.start;
            /* reuse current Tag[] element */
            range.start = lineOffset;
            range.length -= item.length;
          }
          else {
            /* start < end <= lineOffset, use whole range */
            item = range;
            range = null;
          }
        }
      }
      else {
        /* out of tags[] range, handle trailing gap */
        item = new StyleRange(defaultStyle);
        item.start = textOffset;
        item.length = lineOffset - item.start;
      }
      
      list.add(item);
      textOffset = item.start + item.length;
      
      if (textOffset == lineOffset) {
        /* at end of line, make StyleRange array for line */
        lines[lineIndex++] = list.toArray(new StyleRange[list.size()]);
        list.clear();
        lineOffset += columns;
      }
    }
  }

  static int min(int a, int b) {
    return (a < b) ? a : b;
  }

  /***
   *   StyleRange:
   *               int fontStyle
   *               int length
   *               int start
   *
   *             Color background
   *             Color borderColor
   *               int borderStyle
   *              Font font
   *             Color foreground
   *      GlyphMetrics metrics
   *               int rise
   *           boolean strikeout
   *             Color strikeoutColor
   *           boolean underline
   *             Color underlineColor
   *               int underlineStyle
   */
  static StyleRange style(TextStyle defaultStyle, Viewport tag, int i) {
    StyleRange s = new StyleRange(defaultStyle);
    s.start = tag.start(i);
    s.length = tag.length(i);
    s.foreground = color(tag.getForeground(i), defaultStyle.foreground);
    s.background = color(tag.getBackground(i), defaultStyle.background);
    s.underline = tag.isUnderscored(i);
    s.strikeout = tag.isReversed(i);
    return s;
  }

  static Color color(int tagIndex, Color defaultColor) {
    return (tagIndex > 0)
      ? Display.getDefault().getSystemColor(ColorMap.color(tagIndex & 0x0f))
      : defaultColor;
  }

}
