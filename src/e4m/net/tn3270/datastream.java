package e4m.net.tn3270;

import java.nio.ByteBuffer;

public class datastream extends Tn3270Content {
    
  public interface Viewport {
    ByteBuffer datastream();
    byte[] header();
    int command();
    int cursor();
    boolean restoreKeyboard();
    boolean resetModifiedDataTags();
    boolean soundAlarm();
    int rows();
    int columns();
    Text fields();
  }

  public interface Text {
    ByteBuffer text();

    int index(int offset);   // field index for offset
    int count();

    int prefix(int i);
    int start(int i);
    int end(int i);
    int length(int i);

    boolean isEditable(int i);
    boolean isNumeric(int i);
    boolean isSkipped(int i);
    boolean isVisible(int i);
    boolean isBright(int i);
    boolean isHidden(int i);
    boolean isModified(int i);
    boolean isSelectable(int i);

    boolean isHighlighted(int i);
    boolean isBlinking(int i);
    boolean isReversed(int i);
    boolean isUnderscored(int i);

    boolean isMandatoryFill(int i);
    boolean isMandatoryEntry(int i);
    boolean isTrigger(int i);

    boolean isXORed(int i);
    boolean isORed(int i);
    boolean isTransparent(int i);

    int getForeground(int i);
    int getBackground(int i);

    int getCharsetId(int i);

    void setModified(int i, boolean mdt);     // to set Modified Data Tag
  }

}
