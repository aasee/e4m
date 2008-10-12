# make JDK_HOME=/opt/local/jdk1.6.0

jsh: main.c makeargs.o
	gcc -O2 -std=c99 \
		-rdynamic -ldl -s \
		-I${JDK_HOME}/include -I${JDK_HOME}/include/linux \
		-D_MAIN_JAR="jsh.jar" \
		-D_MAIN_CLASS="jsh/Main" \
		-o jsh \
		main.c makeargs.o

makeargs.o: makeargs.c
	gcc -O2 -std=c99 \
		-o makeargs.o \
		-c makeargs.c
