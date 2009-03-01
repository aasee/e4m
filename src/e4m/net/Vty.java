package e4m.net;

import e4m.net.tn3270.datastream.Text;
import e4m.ui.Codec;

public interface Vty {
  
  void open();
  void close();

  void setSize(int rows, int columns);
  void setCodec(Codec codec);

  void reset(boolean kb, boolean mdt);
  void update(int command, int cursor, Text fields);

  void setAttentionListener(AttentionListener listener);
  
  interface AttentionListener {
    void attention(int aid, int cursor, Text fields);
  }

}
