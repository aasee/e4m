#include <stdio.h>
#include <string.h>

char ebcdic[] =  "................"  /* 0 */
                 "................"  /* 1 */
                 "................"  /* 2 */
                 "................"  /* 3 */
                 " ...........<(+|"  /* 4 */
                 "&.........!$*);."  /* 5 */
                 "-/.........,%_>?"  /* 6 */
                 "..........:#@'=\"" /* 7 */
                 ".abcdefghi......"  /* 8 */
                 ".jklmnopqr......"  /* 9 */
                 "..stuvwxyz......"  /* a */
                 "................"  /* b */
                 ".ABCDEFGHI......"  /* c */
                 ".JKLMNOPQR......"  /* d */
                 "..STUVWXYZ......"  /* e */
                 "0123456789......"  /* f */
                 ;
                      /* 4a = cent sign   */
                      /* 5f = logical not */

int hexa(char c) {
  if (c < '0'  ) return 0;      /* x30:x39 */
  if (c < '9'+1) return c - 48; /* 48      */
  if (c < 'A'  ) return 0;      /* x41:x46 */
  if (c < 'F'+1) return c - 55; /* 65 - 10 */
  if (c < 'a'  ) return 0;      /* x61:x66 */
  if (c < 'f'+1) return c - 87; /* 97 - 10 */
  return 0;
}

int main(int argc, char** args) {

  int i;
  char buf[256], *p, *q;

  while (fgets(buf,sizeof(buf),stdin)) {

    if (buf[0] == 0x09) {

      for (i = strlen(buf) - 1; i < 90; i++)
        buf[i] = ' ';

      p = buf + 10;
      q = buf + 69;

      for (;;) {
        if (*p == ' ') p++;
        if (*p == ' ') break;
        i = (hexa(*p++) << 4) | hexa(*p++);
        *q++ = ebcdic[i];
      }
      *q++ = '\n';
      *q = 0;
    }

    fputs(buf,stdout);
    fputs(buf,stderr);
  }

  return 0;
}

/*
0123456789abcdefABCDEF
0----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9

	0x0000:  4500 0030 382a 4000 4006 c76a 8060 8270  E..08*@.@..j.`.p  ................

          10   15   20   25   30   35   40   45                      69

0x09
*/
