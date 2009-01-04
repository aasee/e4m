package e4m.net;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Map;

import static e4m.net.URIQuery.*;

public final class RequestProperties {

  static
  public void add(URLConnection c, String ... list) {
    for (int i = 0; i < list.length; i++) {
      c.addRequestProperty( list[i+0], list[i+1] );
      i++;
    }
  }
  
  static
  public void add(URLConnection c, String[] ... pairs) {
    for (String[] kv : pairs)
      c.addRequestProperty(kv[0],kv[1]);
  }
 
  static
  public void set(URLConnection c, String ... list) {
    for (int i = 0; i < list.length; i++) {
      c.setRequestProperty( list[i+0], list[i+1] );
      i++;
    }
  }
  
  static
  public void set(URLConnection c, String[] ... pairs) {
    for (String[] kv : pairs)
      c.setRequestProperty(kv[0],kv[1]);
  }
  
  static
  public void mapQuery(URLConnection c, String ... list) throws MalformedURLException {
    mapQuery(c,synonym(list));
  }
  
  static
  public void mapQuery(URLConnection c, String[] ... pairs) throws MalformedURLException {
    mapQuery(c,synonym(pairs));
  }
 
  static
  public void mapQuery(URLConnection c, Map<String,String> alias) throws MalformedURLException {
    try {
      for (String[] kvp : query(c.getURL().toURI().getRawQuery())) {
        c.setRequestProperty( (alias.containsKey(kvp[0]) ? alias.get(kvp[0]) : kvp[0]), kvp[1] );
      }
    }
    catch (URISyntaxException e) {
      MalformedURLException me = new MalformedURLException(e.toString());
      me.setStackTrace(e.getStackTrace());
      throw me;
    }
  }
  
}