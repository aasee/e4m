importClass(java.lang.System);
importPackage(java.io);

importPackage(Packages.e4m.js.trace);
importPackage(Packages.e4m.io);

var remote = arguments[0];

var f = new FileReader("tcpdump.log");
var r = new TcpdumpReader(f);

var t = new SnapOutputStream(new OutputStreamWriter(System.out));

for (;;) {
  var p = r.readPacket();
  if (!p) break;

  var s = p.source();
  var d = p.destination();

  if (d.equals(remote))
    println(p.timestamp()+'  '+s+' -> '+d+'  '+p.flags());
  else
    println(p.timestamp()+'  '+d+' <- '+s+'  '+p.flags());

  var x = p.data();
  if (x) {
    t.writeBytes(x);
    t.flush();
    t.reset();
  }

  println('');
}


/*

  var data = p.data();
  var buf = java.lang.reflect.Array.newInstance(java.lang.Byte.TYPE,data.length);
  ios.read(buf);
  if (Arrays.equals(data,buf)) {
    println("compare: "+buf.length);
  }

*/
