package e4m.log;

import java.util.logging.Logger;
import java.util.logging.LogRecord;

/*
    D.bg(1,"a debug message");
    
    if (D.bg)
      D.bg("a debug message");

    message contains calling class/method 
*/

public class D {

  static Logger log = L.log;
  static int    vbl = L.vbl;

  public static void setLogger(Logger l) { log = l; }
  public static void setVerbosity(int v) { vbl = v; }

  public static boolean bg = false;

  public static void bg(String msg) {
    bg(0,1,msg);
  }

  public static void bg(int level, String msg) {
    bg(level,1,msg);
  }

  public static void bg(int level, String msg, Throwable thrown) {
    bg(level,1,msg,thrown);
  }

  public static void bg(int level, int depth, String msg) {
    if (level > vbl) return;
    logr(level,msg, null ,( (depth < 1) ? null :
                            Thread.currentThread()
                                  .getStackTrace()[ depth + 1 ] ) );
  }

  public static void bg(int level, int depth, String msg, Throwable thrown) {
    if (level > vbl) return;
    logr(level,msg,thrown,( (depth < 1) ? null :
                            Thread.currentThread()
                                  .getStackTrace()[ depth + 1 ] ) );
  }

  static void logr(int level, String msg, Throwable thrown, StackTraceElement ste) {
    LogRecord r = new LogRecord(L.vl[level],msg);
    r.setSourceClassName(null);
    r.setSourceMethodName(ste.toString());
    r.setThrown(thrown);
    log.log(r);
  }
  
  public static void enable() {
    L.enable();
    D.vbl = Integer.getInteger("verbose.debug",0);
    D.bg  = (D.vbl > 0);
  }

}
