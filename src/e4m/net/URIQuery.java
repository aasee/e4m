package e4m.net;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIQuery {

  static
  Pattern kv = Pattern.compile("([^=&]+)=([^&]*)&*");
  
  static
  public Iterable<String[]> query(final CharSequence ch) {
    return new Iterable<String[]>() {
      
      Matcher m = kv.matcher(ch);
      boolean found = false;

      @Override
      public Iterator<String[]> iterator() {
        return new Iterator<String[]>() {

          @Override
          public boolean hasNext() {
            return (found = m.find());
          }

          @Override
          public String[] next() {
            if ( ! found && ! hasNext()) {
              throw new NoSuchElementException();
            }
        
            String[] kvp = new String[2];
            kvp[0] = m.group(1);
            kvp[1] = m.group(2);

            if (m.groupCount() != 2 || kvp[0].length() == 0) {
              throw new IllegalArgumentException(m.group(0));
            }
          
            try {
              if (kvp[0].length() > 0) kvp[0] = URLDecoder.decode(kvp[0],"UTF-8");
              if (kvp[1].length() > 0) kvp[1] = URLDecoder.decode(kvp[1],"UTF-8");
            }
            catch (UnsupportedEncodingException e) {
              IllegalArgumentException iae = new IllegalArgumentException(e.toString());
              iae.setStackTrace(e.getStackTrace());
              throw iae;
            }

            return kvp;
          }

          @Override
          public void remove() { throw new UnsupportedOperationException(); }
        };
      }  
    };
  }

  static
  public Map<String,String> synonym(String ... list) {
    Map<String,String> synonym = new HashMap<String,String>();
    for (int i = 0; i < list.length; i++) {
      synonym.put( list[i+1], list[i+0] );
      i++;
    }
    return synonym;
  }

  static
  public Map<String,String> synonym(String[] ... pairs) {
    Map<String,String> synonym = new HashMap<String,String>();
    for (String[] pair : pairs) {
      synonym.put(pair[1],pair[0]);
    }
    return synonym;
  }
  
}
