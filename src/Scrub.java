
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.Writer;
import java.util.Arrays;

public class Scrub {
    
    // Scrub <in.file> <out.file> <name1/alias1> <name2/alias2>
    
    public static void main(String[] a) throws Exception {
        new Scrub().start(a);
    }
    
    BufferedReader in;
    Writer out;
    String[][] alias;
    String lineIn;
    char[] lineOut = new char[86];
    
    void start(String[] a) throws Exception {
        
        in = new BufferedReader(new FileReader(a[0]));
        out = new FileWriter(a[1]);

        alias = new String[][] { a[2].split("/"),
        		                 a[3].split("/") };
        
        while ( skip() ) {                          // find next header
            int b = copy(1);                        // get 'Version / IHL' byte
            scrub( 1 );                             // skip 'Type Of Service'
            int dl = (copy(1) << 8) | copy(1);      // get 'Total Length'
            int ihl = ( (b & 0x0f) * 4 );           // compute IP header length in bytes
            scrub( ihl - (1 + 1 + 1 + 1) );         // scrub rest of IP header
            scrub( 3 * 4 );                         // scrub first part of TCP header
            int c = copy(1);                        // get 'Data Offset' byte
            int thl = ( ((c >> 4) & 0x0f) * 4 );    // compute TCP data offset
            scrub( thl - ((3 * 4) + 1) );           // scrub rest of TCP header
            copy( dl - (ihl + thl) );               // copy remaining data
        }
        
        in.close();
        out.close();
    }

    String alias(String name) {
        if (name.equals(alias[0][0])) return alias[0][1];
        if (name.equals(alias[1][0])) return alias[1][1];
        return null;
    }

    //        [0]     [1]     [2]   [3]     [4]      [5]
    // hh:mm:ss.uuuuu IP source.port > target.port: flags ...

    boolean skip() throws IOException {
        if (ep > 69) {
            out.write(lineOut,0,ep);
            out.write('\n');
            ep = 0;
        }

        while ((lineIn = in.readLine()) != null) {
            String[] hdr = lineIn.split(" ");
            if (hdr.length > 5 && hdr[1].equals("IP")) {
                out.append(hdr[0]).append(' ')
                   .append(hdr[1]).append(' ')
                   .append(alias(hdr[2])).append(' ')
                   .append(hdr[3]).append(' ')
                   .append(alias(hdr[4].substring(0,hdr[4].length()-1)))
                   .append(": ").append(hdr[5]).append(" ...\n");
                
                next();
                return true;
            }
            else {                                
                out.append(lineIn)
                   .append('\n');
            }
        }
        return false;
    }
        
    int ip = 10;
    int op = 10, ap = 51, ep = 69;
    
    char[] hex = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    
    int hex(char c) {
        return (c < 'a') ? (c - '0') : ((c - 'a') + 10); 
    }
    
    int get() throws IOException {
        if (ip > 48) {
            out.write(lineOut,0,ep);
            out.write('\n');
            next();
        }
        int b = (hex(lineIn.charAt(ip++)) << 4) | hex(lineIn.charAt(ip++));
        if (((ip - 9) % 5) < 1) ip++;
        return b;
    }
    
    void put(int b) {
        lineOut[op++] = hex[ (b >> 4) & 0x0f ];
        lineOut[op++] = hex[ (b     ) & 0x0f ];
        if (((op - 9) % 5) < 1) op++;
        lineOut[ap++] = ascii(b);
        lineOut[ep++] = ebcdic(b);
    }

    void next() throws IOException {        
        lineIn = in.readLine();
        lineIn.getChars(0,8,lineOut,0);
        Arrays.fill(lineOut,8,lineOut.length,' ');
        
        ip = 10;
        op = 10;
        ap = 51;
        ep = 69;
    }
    
    char ascii(int a) {
        return (a < ' ' || '~' < a) ? '.' : (char)a;
    }
    
    char ebcdic(int e) {
        return (e < 0x040) ? '.' : ebcdic.charAt( e - 0x040 );
    }

    String ebcdic = " ...........<(+|"  + // 40
                    "..........!$*);."  + // 50
                    "-/.........,%_>?"  + // 60
                    ".........`:#@'=\"" + // 70
                    ".abcdefghi......"  + // 80
                    ".jklmnopqr......"  + // 90
                    ".~stuvwxyz......"  + // A0
                    "................"  + // B0
                    "{ABCDEFGHI......"  + // C0
                    "}JKLMNOPQR......"  + // D0
                   "\\.STUVWXYZ......"  + // E0
                    "0123456789......"  ; // F0

    void scrub(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            get();
            put(0);
        }
    }
    
    int copy(int n) throws IOException {
        int b = -1;
        for (int i = 0; i < n; i++) {
            b = get();
            put(b);
        }
        return b;
    }

}
