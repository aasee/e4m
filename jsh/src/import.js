#!/usr/local/bin/jsh -Djava.class.path=../bin

// Packages.java;
importClass(java.lang.System);
println(System.getProperty("java.class.path"));

Packages.jsh;
importClass(Packages.jsh.Main);
var m = new Main();
println("m: "+m);

