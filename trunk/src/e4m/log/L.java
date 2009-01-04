package e4m.log;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/*
    L.og(1,"a log message");
*/

public class L {
  
  static Logger log = stderr();
  static int    vbl = 0;  //          verbosity -> Level.value

  static Level vl[] = { Level.SEVERE,     //  0 -> 1000
                        Level.WARNING,    //  1 -> 900
                        Level.INFO,       //  2 -> 800
                        Level.CONFIG,     //  3 -> 700
                        Level.FINE,       //  4 -> 500
                        Level.FINER,      //  5 -> 400
                        Level.FINEST };   //  6 -> 300

  public static Logger stderr() {
    Logger l = Logger.getAnonymousLogger();
    l.setUseParentHandlers(false);
    l.addHandler( new Handler() {
                    public void close() {}
                    public void flush() {
                      System.err.flush();
                    }
                    public void publish(LogRecord record) {
                      System.err.println(record.getMessage());
                    }
                  } );
    return l;
  }
  
  public static void setLogger(Logger l) { log = l; }
  public static void setVerbosity(int v) { vbl = v; }

  public static Level levelFor(int verbosity) {
    return vl[verbosity];
  }

  public static void og(String msg) {
    og(0,msg);  
  }
  
  public static void og(int level, String msg) {
    if (level > vbl) return;
    log.log(L.vl[level],msg);
  }

  public static void og(int level, String msg, Throwable thrown) {
    if (level > vbl) return;
    log.log(L.vl[level],msg,thrown);
  }

  public static void enable() {
    L.vbl = Integer.getInteger("verbose.log",0);
  }
  
}

