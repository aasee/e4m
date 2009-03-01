package e4m.js.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import java.net.URL;

import javax.swing.text.AttributeSet;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Element;
import javax.swing.text.html.parser.Parser;
import javax.swing.text.html.parser.TagElement;

public class HtmlKit extends Parser {

    public interface Handler {
        void handleStartTag(Element tag, AttributeSet atts);
        void handleEndTag(Element tag);
        void handleText(String text);
        void handleComment(String text);
        void handleError(int ln, String msg);
    }

    Handler handler;
    boolean start;

    public HtmlKit() throws IOException {
        super(DTD.getDTD("html32"));
    }

    public void parse(String url, Handler callback) throws IOException {
        parse(reader(url),callback);
    }

    public void parse(File src, Handler callback) throws IOException {
        parse(reader(src),callback);
    }

    public void parse(Reader reader, Handler callback) throws IOException {
        this.handler = callback;
        parse(reader);
    }

    @Override
    protected void startTag(TagElement tag) throws ChangedCharSetException {
        start = true;
        super.startTag(tag);
        flushAttributes();
        start = false;
    }    
    @Override
    protected void handleEmptyTag(TagElement tag) {
        if (start)
            handler.handleStartTag(tag.getElement(),getAttributes());
        else
            handler.handleEndTag(tag.getElement());
    }
    @Override
    protected void handleStartTag(TagElement tag) {
        handler.handleStartTag(tag.getElement(),getAttributes());
    }
    @Override
    protected void handleEndTag(TagElement tag) {
        handler.handleEndTag(tag.getElement());
    }
    @Override
    protected void handleText(char[] text) {
        handler.handleText(new String(text));
    }
    @Override
    protected void handleComment(char[] text) {
        handler.handleComment(new String(text));
    }
    @Override
    protected void handleError(int ln, String msg) {
        handler.handleError(ln,msg);
    }

    public InputStream stream(URL url) throws IOException {
        return url.openStream();
    }

    public InputStream stream(String url) throws IOException {
        return stream(new URL(url));
    }

    public Reader reader(URL url) throws IOException {
        return new InputStreamReader(stream(url));
    }

    public Reader reader(String url) throws IOException {
        return reader(new URL(url));
    }

    public Reader reader(File file) throws IOException {
        return new FileReader(file);
    }

    public File store(String url) throws IOException {
        return store(new URL(url));
    }

    public File store(URL url) throws IOException {
        File file = cache(url.getHost(),url.getPath());
        OutputStream out = new FileOutputStream(file);
        InputStream in = stream(url);
        int b; while ((b = in.read()) != -1) out.write(b);
        in.close();
        out.close();
        return file;
    }

    public File cache(String root, String path) throws IOException {
        return cache(root+File.separatorChar+path);
    }

    public File cache(String filename) throws IOException {
        File file = new File(filename);
        File dirs = file.getParentFile();
        if (dirs != null) dirs.mkdirs();
        return file;
    }

    public File move(File src, String dst) throws IOException {
        return move(src,cache(dst));
    }

    public File move(File src, File dst) throws IOException {
        return src.renameTo(dst) ? dst : src;
    }

 }

/*

importClass(e4m.html.HtmlKit);

var hp = new HtmlKit();

var cb =
  new e4m.html.HtmlKit.Handler() {
    handleComment: function(text) {}
    handleEmptyTag: function(tag) {}
    handleEndTag: function(tag) {}
    handleError: function(ln,msg) {}
    handleStartTag: function(tag) {}
    handleText: function(text) {}
  };

var url = "http://localhost/index.html";

hp.parse(url,cb);

*/
