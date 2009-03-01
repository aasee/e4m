package e4m.ui.swt;

import e4m.net.Pty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import e4m.net.Vty;
import e4m.net.tn3270.Tn3270Terminal;
import e4m.net.tn3270.datastream.Text;
import e4m.ui.Codec;

public class Tty implements Vty {
	
  public static void main(String[] a) throws Exception {
    String url = a[0];

    Pty pty = new Tn3270Terminal();
    Vty vty = new Tty();

    pty.connect(url);
    attach(vty,pty);
    pty.configure(vty);
    pty.poll(vty);
    vty.close();
  }

  static void attach(final Vty vty, final Pty pty) {
    new Thread( new Runnable() {
      public void run() {
        try {
          vty.open();
          pty.disconnect();
        }
        catch (Exception e) {
          RuntimeException re = new RuntimeException(e.toString(),e);
          re.setStackTrace(e.getStackTrace());
          throw re;
        }
      }
    }).start();
    pause();
  }

  static void pause() { try { Thread.sleep(1000); } catch (Exception e) {} }
  
  Shell shell;
  Display display;
  StyledText text;
  Label oia;
  Controller controller;
  Font font;
  
  @Override
  public void open() {
    // create the widget's shell
    display = new Display();    
    shell = new Shell( display, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX );

    shell.setSize(1,1);
    shell.setLayout(new RowLayout(SWT.VERTICAL));

    font =  new Font( display,
                      System.getProperty("Monospace.name","Monospace"),
                      Integer.getInteger("Monospace.size", 8),
                      SWT.NORMAL);

    // create the styled text widget
    text = new StyledText(shell,SWT.BORDER);

    oia = new Label(shell,SWT.SHADOW_IN|SWT.BORDER);
    oia.setFont(font);

    controller = new Controller();
    controller.setWidget(text,font);

    shell.open();
    while ( ! shell.isDisposed()) {
      if ( ! display.readAndDispatch()) {
        display.sleep();
      }
    }  
    display.dispose();
  }

  @Override
  public void close() {
    display.syncExec(new Runnable() {
      public void run() {
        display.close();      
      }});
  }
   
  @Override
  public void update(final int command, final int cursor, final Text fields) {
    display.syncExec(new Runnable() {
      public void run() {
        controller.updateFields(command,cursor,fields);    
      }});
  }

  @Override
  public void setAttentionListener(final AttentionListener listener) {
    display.syncExec(new Runnable() {
      public void run() {
        controller.setAttentionListener(listener);    
      }});
  }

  @Override
  public void reset(final boolean kb, final boolean mdt) {
    display.syncExec(new Runnable() {
      public void run() {
        controller.reset(kb,mdt);
      }});
  }
  
  @Override
  public void setSize(final int rows, final int columns) {
    display.syncExec(new Runnable() {
      public void run() {
        controller.setSize(rows,columns);
        Point s = text.getSize();
        Point o = oia.getSize();
        oia.setSize(s.x,o.y);
        shell.pack();
      }});
  }

  @Override
  public void setCodec(final Codec codec) {
    display.syncExec(new Runnable() {
      public void run() {
        controller.setCodec(codec);    
      }});
  }
  
}
