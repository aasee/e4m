set PATH=c:\Windows;c:\Java\jdk6\bin
jrunscript -cp ..\..\build\classes peer.js localhost 24 S 127.0.0.1.23 < ..\trc\tcpdump.log > out.server
