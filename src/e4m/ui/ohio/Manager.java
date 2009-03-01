package e4m.ui.ohio;

import java.io.IOException;
import java.util.Arrays;
import org.ohio.OhioManager;
import org.ohio.OhioSession;
import org.ohio.OhioSessions;

class Manager implements OhioManager {

  OhioSession[] sessions;
  int count;

  Manager() {
    this.sessions = new OhioSession[8];
    this.count = 0;
  }

  public OhioSessions getSessions() {
    return new OhioSessions() {
      public OhioSession getItem(int index) {
        return (index < 0 || count < index) ? null : sessions[index];
      }
      public OhioSession getItem(String sessionName) {
        int i = find(sessionName); return (i < 0) ? null : sessions[i];
      }    
      public int count() {
        return count;
      }
      public void refresh() {}
    };
  }

  public OhioSession openSession(String configurationResource, String sessionName) throws IOException {

    // TODO: implement function logic in OhioSession.openSession() javadoc

    OhioSession session = new Session(configurationResource,sessionName);
    session.connect();
    if (session.isConnected()) {
      add(session);
    } else {
      session = null;
    }
    return session;
  }

  public void closeSession(OhioSession sessionObject) throws IOException {
    close(find(sessionObject));
  }

  public void closeSession(String sessionName) throws IOException {
    close(find(sessionName));
  }

  void close(int index) throws IOException {
    if (index < 0) return;
    sessions[index].disconnect();
    sessions[index] = null;
  }

  int find(OhioSession sessionObject) {
    if (sessionObject != null) {
      for (int i = 0; i < sessions.length; i++) {
        if (sessions[i] == sessionObject) return i;
      }
    }
    return -1;
  }

  int find(String sessionName) {
    if (sessionName != null && sessionName.length() > 0) {
      for (int i = 0; i < sessions.length; i++) {
        if ( sessions[i] != null &&
          sessions[i].getSessionName().equals(sessionName) ) return i;
      }
    }
    return -1;
  }

  void add(OhioSession session) {
    for (int i = 0; i < sessions.length; i++) {
      if (sessions[i] == null) {
        sessions[i] = session;
        return;
      }
    }
    int i = sessions.length;
    sessions = Arrays.copyOf(sessions,i+1);
    sessions[i] = session;
  }

}
