package e4m.js.tcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import e4m.io.StandardInput;
import e4m.io.StandardOutput;

public class TcpTask {

  String script;
  ScriptEngineManager manager;
  
  public TcpTask(String scriptname) throws IOException {
    script = loadScript(scriptname);
    manager = new ScriptEngineManager();
  }
  
  public void service(Socket socket) throws IOException, ScriptException {
    ScriptEngine engine = manager.getEngineByName("JavaScript");

    engine.put("stdin", new StandardInput(socket.getInputStream()));
    engine.put("stdout", new StandardOutput(socket.getOutputStream()));
    engine.put("socket", socket);

    engine.eval(script);
  }
  
  String loadScript(String scriptname) throws FileNotFoundException, IOException {
    File file = new File(scriptname);
    char[] buf = new char[(int) file.length()];
    new FileReader(file).read(buf);
    return new String(buf);
  }

}
