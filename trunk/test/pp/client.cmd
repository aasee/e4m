set PATH=c:\Windows;c:\Java\jdk6\bin
jrunscript -cp ..\..\build\classes peer.js localhost 24 C 127.0.0.1.37363 < ..\trc\tcpdump.log > out.client
