importClass(java.lang.System);
importClass(java.util.Arrays);
importPackage(java.io);

importPackage(Packages.e4m.io);
importPackage(Packages.e4m.net.script);
importPackage(Packages.e4m.net.trace);

if (arguments.length != 5) {
  println("usage:   peer.js  <host>  <port>  <logfile>  <mode>  <id>");
  quit();
}

var hostname = arguments[0];
var portnum = arguments[1];
var logfile_name = arguments[2];
var socket_mode = arguments[3];
var socket_id = arguments[4];

var log = new TcpdumpReader(new FileReader(logfile_name));
var out = new SnapOutputStream(new OutputStreamWriter(System.out));

var ios;

for (;;) {
  var p = log.readPacket();
  if (!p) break;

  var f = p.flags();

  if (f.equals("S")) {
    if (p.source().equals(socket_id)) {
      syn();
    }
  }
  else if (f.equals("P")) {
    if (p.destination().equals(socket_id)) {
      destination(p);
    }
    else if (p.source().equals(socket_id)) {
      source(p);
    }
  }
}
quit();


function syn() {
  if (socket_mode.equals("S")) {
    ios = Tcp.listen(hostname,portnum);
  }
  else if (socket_mode.equals("C")) {
    ios = Tcp.connect(hostname,portnum);
  }
  else {
    println("invalid program mode ["+socket_mode+"]");
    quit();
  }
}

function source(p) {
  println("source: "+p.timestamp());
  var buf = p.data();
  ios.write(buf);
  println("sent: "+buf.length);
}

function destination(p) {
  println("destination: "+p.timestamp());
  var data = p.data();
  var buf = Tcp.alloc(data.length);
  var n = ios.read(buf);
  if (n == data.length && Tcp.cmp(data,buf)) {
    println("received: "+data.length);
  }
  else {
    println("received:");
    out.writeBytes(x);
    out.flush();
    out.reset();
  }
}
