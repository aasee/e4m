package e4m.util;

public class Task {

  public static Object start(Runnable runnable) {
    new Thread(runnable).start();
    try { Thread.sleep(1000); }
      catch (InterruptedException e) {}
    return runnable;
  }
  
}
