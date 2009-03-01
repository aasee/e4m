rd /s /q htm
md htm
set PATH=c:\Windows;c:\java\jdk6\bin
java -cp ..\..\build\classes Sim 127.0.0.1.9000 tn3270://localhost:24/?MODEL=IBM-3278-2-E 24 80 4096 < tso.trc > try.out 2>&1
