package jsh;

import javax.script.*;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
  public static void main(String[] argv) throws Exception {

    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("JavaScript");

    engine.put("ENGINE",engine);
    engine.put("ARGV",argv);

    engine.put("STDIN",System.in);
    engine.put("STDOUT",System.out);
    engine.put("STDERR",System.err);

    InputStream js = ClassLoader.getSystemClassLoader()
                                .getResourceAsStream("jsh/shell.js");
    if (js != null)
      engine.eval(new InputStreamReader(js));

    String home = System.getProperty("user.home");
    File rc = new File(home,".jshrc");
    if (rc.exists())
      engine.eval(new FileReader(rc));

    engine.eval(new ScriptReader(new FileReader(argv[0])));
  }
}
