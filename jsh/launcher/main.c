#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <jni.h>

#include "makeargs.h"

// 'stringification' helper macros

#define xstr(s) str(s)
#define str(s) #s

#define MAIN_JAR xstr(_MAIN_JAR)
#define MAIN_CLASS xstr(_MAIN_CLASS)

#define PASS 1
#define FAIL 0

void ParseArgs(int,char**);
void Cleanup(void);
int StartVM(void);
void StopVM(void);
void RunMain(void);
int FindVM(void);
int AllocVMLib(void);
void FreeVMLib(void);
void SetVMOptions(void);
int FindMain(void);
int FindMainClass(void);
int SetArg(int,char*);
int SetMainArgs(void);
void CheckClassPath();
char* MainClassPath();

void say(const char*, ...);
char* filename(char*,char*);


jint rc;
char* error;

int main( int argc, char* argv[] ) {
  ParseArgs(argc,argv);
  if ( StartVM() ) {
    RunMain();
    StopVM();
  }
  Cleanup();
  return 0;
}

JNIEnv *env;
JavaVM *jvm;
JavaVMInitArgs jvm_args;
JavaVMOption *jvm_options;

jint (*_CreateJavaVM)(JavaVM **pvm, JNIEnv **penv, void *vm_args);

int StartVM() {
  if ( FindVM() ) {
    SetVMOptions();
    jvm_args.version = JNI_VERSION_1_2;
    jvm_args.ignoreUnrecognized = JNI_TRUE;
    rc = (*_CreateJavaVM)( &jvm, &env, &jvm_args );
    if ( ! rc ) {
      return PASS;
    }
    say( "JNI_CreateJavaVM rc=%d", rc );
  }
  return FAIL;  
}

void StopVM() {
  if ( (*env)->ExceptionOccurred(env) ) {
    (*env)->ExceptionDescribe(env);
  }
  (*jvm)->DestroyJavaVM(jvm);
  FreeVMLib();
}
    
char* interpreter;
int interpreter_argc = 0;
char** interpreter_argv;
int command_argc;
char** command_argv;

void ParseArgs( int argc, char* argv[] ) {
  char** argp = argv;
  interpreter = *argp++; argc--;
  if (*(*argp) == '-') {
    struct ARGS *p = makeargs(*argp);
    interpreter_argc = p->argc;
    interpreter_argv = p->argv;
    argp++; argc--;
  }
  command_argc = argc;
  command_argv = argp;
  CheckClassPath();
}
  
void Cleanup() {
  // TODO: any local cleanup
}

void *VMLib;

int AllocVMLib() {
  char* libpath = filename( interpreter, "libjvm.so" );
  VMLib = dlopen( libpath, RTLD_NOW );
  free( libpath );
  if ( VMLib ) {
    return PASS;
  }
  say( dlerror() );
  return FAIL;
}

void FreeVMLib() {
  if ( VMLib ) {
    rc = dlclose(VMLib);
    if ( rc ) {
      say( "%s  rc=%d", dlerror(), rc );
    }
  }
}

int FindVM() {
  if ( AllocVMLib() ) {
    dlerror();
    *(void**) (&_CreateJavaVM) = dlsym( VMLib, "JNI_CreateJavaVM" );
    error = dlerror();
    if ( error == NULL ) {
      return PASS;
    }
    say( error );
  }
  return FAIL;
}

void SetVMOptions() {
  int n = 1 + interpreter_argc;
  JavaVMOption *options = malloc( n * sizeof(*options) );
  if ( options ) {
    jvm_args.nOptions = n;
    jvm_args.options = options;
    options[0].optionString = MainClassPath();
    options[0].extraInfo = 0;
    for ( n = 0; n < interpreter_argc; n++ ) {
      // printf("vm[%i] '%s'\n",n,interpreter_argv[n]);
      options[n+1].optionString = interpreter_argv[n];
      options[n+1].extraInfo = 0;
    }
  }
}

jclass main_class;
jmethodID main_id;
jobjectArray main_args;
    
void RunMain() {    
  if ( FindMain() && SetMainArgs() ) {
    (*env)->CallStaticVoidMethod( env, main_class, main_id, main_args );
  }
} 

int FindMainClass() {
  main_class = (*env)->FindClass( env, MAIN_CLASS );
  if ( main_class ) {
    return PASS;
  }
  say( "Could not find Main-Class: %s", MAIN_CLASS );
  return FAIL;
}

int FindMain() {
  if ( FindMainClass() ) {
    main_id = (*env)->GetStaticMethodID( env, main_class,
                                              "main",
                                              "([Ljava/lang/String;)V" );
    if ( main_id )  {
      return PASS;
    }
    say( "Could not find %s.main()", MAIN_CLASS );
  }
  return FAIL;
}

int SetArg( int index, char* argv ) {
  jstring arg = (*env)->NewStringUTF( env, argv );
  if ( arg ) {
    (*env)->SetObjectArrayElement( env, main_args, index, arg );
    if ( (*env)->ExceptionCheck(env) != JNI_TRUE ) {
      return PASS;
    }
  }
  return FAIL;
}

int SetMainArgs() {
  jclass String = (*env)->FindClass(env, "java/lang/String");
  if ( String ) {
    main_args = (*env)->NewObjectArray( env, command_argc, String, 0);
    if ( main_args ) {
      for ( int i = 0; i < command_argc; i++) {
        if ( ! SetArg( i, command_argv[i] ) ) {
          break;
        }
      }
      return PASS;
    }
  }
  say( "Out of memory" );
  return FAIL;
}

char* class_path_option = "-Djava.class.path=";
char* user_class_path = 0;
  
void CheckClassPath() {
  int n = strlen( class_path_option );
  for ( int i = 0; i < interpreter_argc; i++ ) {
    if ( strncmp( interpreter_argv[i], class_path_option, n ) == 0) {
      char* p = interpreter_argv[i] + 2;
      *p++ = 'u'; *p++ = 's'; *p++ = 'e'; *p++ = 'r';
      user_class_path = interpreter_argv[i] + n;
      return;
    }
  }
}

char* MainClassPath() {
  int n = strlen( class_path_option );
  char* jar_path = filename( interpreter, MAIN_JAR );
  int j = strlen( jar_path ) + 1;
  int u = user_class_path ? strlen( user_class_path ) + 1 : 0;
  char* cp = malloc( n+j+u );
  if ( cp ) {
    strncpy( cp, class_path_option, n );
    strcpy( cp+n, jar_path );
    if ( u ) {
      *(cp+n+(j-1)) = ':';
      strcpy( cp+n+j, user_class_path );
    }
  }
  free( jar_path );
  return cp;
}

char* filename( char* path, char* name ) {    
  int n;
  char* p = strrchr( path, '/' );
  if ( p ) 
    n = ((int)p - (int)path) + 1;
  else {
    n = 2; path = "./";
  }
  p = malloc( n + strlen( name ) + 1 );
  strncpy( p, path, n );
  strcpy( p+n, name );
  return p;
}

void say( const char* fmt, ... ) {
  va_list vp;
  va_start( vp, fmt );
  vfprintf( stderr, fmt, vp );
  va_end( vp );
  fputc( '\n', stderr );
}

