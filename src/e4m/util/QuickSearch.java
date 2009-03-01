package e4m.util;

import java.util.Arrays;

public class QuickSearch {

    static final int ALPHABET_SIZE = 256;

    short[] skip = new short[ALPHABET_SIZE];

    char[] pattern;

    public QuickSearch pattern(char[] str) {
        pattern = str;
        Arrays.fill(skip, (short)(pattern.length+1));
        for (int i = 0; i < pattern.length; ++i) {
            skip[pattern[i]] = (short)(pattern.length-i);
        }
        return this;
    }

    public int match(char[] text, int start, int length) {
        int offset = start;
        int limit = (start+length) - pattern.length;
        while (offset <= limit) {
            if (match(text,offset)) return offset;
            offset += skip[text[offset+pattern.length]];  /* shift */
        }        
        return -1;
    }

    private boolean match(char[] text, int offset) {
        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i] != text[i+offset]) return false;
        }
        return true;
    }

    public QuickSearch pattern(String str) {
        return pattern(str.toCharArray());
    }

    public int match(String text) {
        return match(text.toCharArray(),0,text.length());
    }

    public static void main( String[] a ) throws Exception {
        int m = new QuickSearch().pattern(a[0]).match(a[1]);
        System.out.println("result: "+m);
    }

}
