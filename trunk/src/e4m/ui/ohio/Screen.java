package e4m.ui.ohio;

import java.io.IOException;
import org.ohio.OhioScreen;
import org.ohio.OhioPosition;
import org.ohio.OhioOIA;
import org.ohio.OhioFields;
import static org.ohio.Ohio.*;

public class Screen implements OhioScreen {

  Fields fields;
  Session session;

  Screen(Session session) {
    this.session = session;
    this.fields = new Fields(this);
  }

  void setSize(int rows, int columns) { fields.setSize(rows,columns); }
  void setCodec(String charsetName) { fields.setCodec(charsetName); }

  OhioOIA oia = new OIA() {
    public Owner getOwner() { return Owner.UNKNOWN; }
    public boolean isNumeric() { return fields.atInput(true); }
    public boolean isAlphanumeric() { return fields.atInput(false); }
  };

  public OhioOIA getOIA() { return oia; }

  public OhioFields getFields() { return fields; }

  public int getRows() { return fields.getRows(); }
  public int getColumns() { return fields.getColumns(); }

  public OhioPosition getCursor() { return fields.getCursor(); }

  public String getString() { return fields.getString(); }

  public void setString(String text, OhioPosition location) {
    fields.setString(text,location);
  }
  
  public byte[] getData(OhioPosition start, OhioPosition end, Plane plane) {
    return fields.getData(start,end,plane);
  }

  public OhioPosition findString(String str, OhioPosition pos, int len,
                                 Direction dir, boolean ignoreCase) {
    return fields.findString(str,pos,len,dir,ignoreCase);
  }


  public void sendKeys(String text, OhioPosition location) {
    // TODO:  write to Session
  }

  public void sendAid(AID aidKey) {
    // TODO:  write to Session
  }

  Object refresh() throws IOException { return session.refresh(); }
  void update(byte[] data) throws IOException { session.update(data); }

}
