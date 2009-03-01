package app;

import e4m.ui.swt.Tty;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

public class Try {

    static Thread subtask;

    static void setUp() throws Exception {
        subtask = ScriptLauncher.launch(
                      new FileReader("test/tso/sim.js"),
                      new String[] {"localhost","24","S","127.0.0.1.23"},
                      new FileReader("test/tso/tso.trc"),
                      new FileWriter("test/tso/sim.out"),
                      new OutputStreamWriter(System.err) );
    }

    static void tearDown() throws Exception {
        if (subtask != null) {
            subtask.interrupt();
            subtask.join();
        }
    }

    public static void main(String[] a) throws Exception {
        setUp();
        System.setProperty("Monospace.name","Courier New");
        System.setProperty("Monospace.size","10");
        Tty.main(new String[] {"tn3270://localhost:24/?MODEL=IBM-3278-2-E"});
        tearDown();
    }
}
