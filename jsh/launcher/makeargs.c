#include <ctype.h>
#include <stdlib.h>
#include "makeargs.h"

struct ARGS* makeargs(char *buf) {
  int b, c, instring;

  c = instring = 0;
  for (b = 0; buf[b]; b++) {
    if (isspace(buf[b])) {
      instring = 0;
    }
    else {
      if ( ! instring) {
        c++;
      }
      instring = 1;
    }
  }

  struct ARGS *args = malloc( sizeof(int) + (c * sizeof(char*)) + (b + 1) );

  args->argc = c;
  char** argv = &(args->argv[0]);
  char* argp = (char*) &(args->argv[c]);

  c = instring = 0;
  for (b = 0; buf[b]; b++) {
    if (isspace(buf[b])) {
      argp[b] = 0;
      instring = 0;
    }
    else {
      if ( ! instring) {
        argv[c++] = argp+b;
      }
      argp[b] = buf[b];
      instring = 1;
    }
  }

  argp[b] = 0;

  return args;
}

/**
#include <stdio.h>
int main(int argc, char* argv[]) {
  for (int i=0; i<argc; i++)
    printf("%d '%s'\n",i,argv[i]);
  struct ARGS *p = makeargs(argv[1]);
  for (int i=0; i<p->argc; i++)
    printf(">%d '%s'\n",i,p->argv[i]);
  return 0;
}
**/
