package app;

import java.io.Reader;
import java.io.Writer;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class ScriptLauncher {

  public static Thread launch( final Reader script, final String[] argv,
                               final Reader stdin,
                               final Writer stdout, final Writer stderr)
  {
    Thread task =
    new Thread(new Runnable() {
      public void run() {
        Object result = start(script,argv,stdin,stdout,stderr);
        checkResult(result);
      }
    });
    task.start();
    try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
    return task;
  }

  public static Object start( Reader script, String[] argv,
                              Reader stdin, Writer stdout, Writer stderr )
  {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");
    ScriptContext context = new SimpleScriptContext();
    context.setReader(stdin);
    context.setWriter(printOut(stdout));
    context.setErrorWriter(stderr);
    context.setAttribute("arguments",argv,ScriptContext.ENGINE_SCOPE);
    context.setAttribute("STOP",STOP,ScriptContext.ENGINE_SCOPE);
    try {
      engine.eval(quit_function,context);
      return engine.eval(script,context);
    }
    catch (Exception ex) {
      return ex;
    }
  }

  static Object STOP = new Object() {
      public void run(String rc) { throw new QUIT(rc); }
  };

  static class QUIT extends RuntimeException{
      QUIT(String r) {super(r);}
  }

  // sun.org.mozilla.javascript.internal.WrappedException:
  //    Wrapped app.ScriptLauncher$QUIT: undefined (<Unknown source>#1) in <Unknown source> at line number 1

  static void checkResult(Object rc) {
    if (rc instanceof Exception) {
      if (rc instanceof ScriptException) {
        String msg = ((Exception)rc).getMessage();
        int i = msg.indexOf("$QUIT");
        if (i > -1) {
          int j = msg.indexOf("at line",i);
          if (j > -1) {
            System.out.println("script "+Thread.currentThread().toString()
                                        +", quit() "+msg.substring(j));
            return;
          }
        }
      }
      ((Exception)rc).printStackTrace();
      return;
    }

    System.out.println(
        "script "+Thread.currentThread().toString()
                 +", result: "+rc);
  }

  static String quit_function = "function quit(code){STOP.run(code);} " +
                                "function exit(code){quit(code);} ";

  public static Object start( Reader script, String[] argv ) {
    return start( script, argv,
                  new InputStreamReader(System.in),
                  new PrintWriter(System.out),
                  new OutputStreamWriter(System.err) );
  }

  public void quit() { throw new RuntimeException(); }

  static Writer printOut(Writer out) {
    return (out instanceof PrintWriter) ? out : new PrintWriter(out);
  }

  public static void main(String[] argv) throws Exception {
    Thread task = launch( new StringReader("println('hello'); "+
                                           "quit(); "),
                          new String[] {"a","b","c"},
                          new InputStreamReader(System.in),
                          new OutputStreamWriter(System.out),
                          new OutputStreamWriter(System.err));
    task.join();
    System.out.println("done");
  }
}