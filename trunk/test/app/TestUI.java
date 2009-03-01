package app;

import e4m.ui.swt.Tty;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import junit.framework.TestCase;

public class TestUI extends TestCase {

    Thread subtask;

    @Override
    public void setUp() throws Exception {
        subtask = ScriptLauncher.launch(
                      new FileReader("test/tso/sim.js"),
                      new String[] {"localhost","24","S","127.0.0.1.23"},
                      new FileReader("test/tso/tso.trc"),
                      new FileWriter("test/tso/sim.out"),
                      new OutputStreamWriter(System.err) );
    }

    @Override
    public void tearDown() throws Exception {
        if (subtask != null) {
            subtask.interrupt();
            subtask.join();
        }
    }

    public void testUI() throws Exception {
        System.setProperty("Monospace.name","Courier New");
        System.setProperty("Monospace.size","10");
        Tty.main(new String[] {"tn3270://localhost:24/?MODEL=IBM-3278-2-E"});
    }
}

/*
    jrunscript -cp build\classes
               sim.js
               localhost 24 S 127.0.0.1.23
               < tso.trc
               > sim.out
    
    java -cp build\classes;lib\swt.jar
         -DMonospace.name="Courier New"
         -DMonospace.size="10"
         e4m.ui.swt.Tty
         tn3270://localhost:24/?MODEL=IBM-3278-2-E
*/
