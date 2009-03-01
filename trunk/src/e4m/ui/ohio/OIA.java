package e4m.ui.ohio;

import java.util.Observable;
import org.ohio.OhioSession;
import org.ohio.OhioOIA;

public abstract class OIA extends Observable implements OhioOIA {

  public class Event {
    OhioSession session;
    Enum type;
    int code;
  }

  Event event;

  public InputInhibited getInputInhibitedType() {
    return (event.type instanceof InputInhibited) ? (InputInhibited)(event.type) : null;
  }

  public int getCommCheckCode() {
    return (event.type == InputInhibited.COMMCHECK) ? event.code : 0;
  }

  public int getMachineCheckCode() {
    return (event.type == InputInhibited.MACHINECHECK) ? event.code : 0;
  }

  public int getProgCheckCode() {
    return (event.type == InputInhibited.PROGCHECK) ? event.code : 0;
  }

}

/*
    interface Observer {

      void update( Observable o, Object arg );

                   Observable -> OIA
                   Object     -> OIA.Event
    }
*/
