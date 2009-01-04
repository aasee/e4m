package e4m.net;

import e4m.net.tn3270.datastream.Viewport;
import e4m.ui.Codec;

public interface Vty {
  
  void start();
  
  void open();
  void close();

  void setSize(int rows, int columns);
  void setCodec(Codec codec);

  void reset(boolean kb, boolean mdt);
  void update(int command, int cursor, Viewport fields);

  void setAttentionListener(AttentionListener listener);
  
  interface AttentionListener {
    void attention(int aid, int cursor, Viewport fields);
  }

}
