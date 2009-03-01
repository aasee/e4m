importPackage(java.io);

importPackage(Packages.e4m.io);
importPackage(Packages.e4m.js.tcp);
importPackage(Packages.e4m.js.trace);

if (arguments.length != 4) {
  println("usage:  peer.js  <host>  <port>  <mode>  <id>");
  quit();
}

var hostname = arguments[0];
var portnum = arguments[1];
var socket_mode = arguments[2];
var socket_id = arguments[3];

var log = new TcpdumpReader(context.getReader());
var out = new SnapOutputStream(context.getWriter());

var ios;

for (;;) {
  var p = log.readPacket();
  if (!p) break;

  if (p.flags().equals("S")) {
    if (p.source().equals(socket_id)) {
      syn();
    }
  }
  else if (p.datalength() > 0) {
    if (p.destination().equals(socket_id)) {
      destination(p);
    }
    else if (p.source().equals(socket_id)) {
      source(p);
    }
  }
}
java.lang.Thread.sleep(3000);
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
  out['write(byte[])'](buf);
  out.flush();
  out.reset();
}

function destination(p) {
  println("destination: "+p.timestamp());
  var data = p.data();
  var buf = Tcp.alloc(data.length);
  var n = ios.readFully(buf);
  println("received: "+buf.length);
  out['write(byte[])'](buf);
  out.flush();
  out.reset();
  if (n == data.length && Tcp.cmp(data,buf)) {
    return;
  }
  println("expecting: "+data.length);
  out['write(byte[])'](data);
  out.flush();
  out.reset();
}

