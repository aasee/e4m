package e4m.net.tn3270;

import java.net.URL;
import java.util.Arrays;

import e4m.net.ProtocolHandler;
import e4m.net.telnet.TelnetConnection;
import e4m.net.tn3270.ds.Partition;

public class Tn3270Connection extends TelnetConnection {

  protected Tn3270Connection(URL url, ProtocolHandler protocol) {
    super(url,protocol);
  }

  Partition partition;

  @Override
  public void setHeaderField(String key, String value) {
    super.setHeaderField(key,value);

    if ("Device-Type".equalsIgnoreCase(key)) {
      if (partition == null) {
        partition = new Partition();
      }
      Device.Model mod = Device.forName(value);
      partition.setSize(mod.rows(),mod.columns());
    }
  }
  
  byte[] serverFunctions = new byte[5];

  void serverFunction(int code) {
    if (code < 0) Arrays.fill(serverFunctions,(byte)0);
    else
      if (code < serverFunctions.length) serverFunctions[code] = 1;
  }

  public boolean serverSupports(int code) {
    return ( 0 <= code && code <= serverFunctions.length && serverFunctions[code] != 0 );
  }

}
