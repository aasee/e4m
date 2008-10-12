
function load(filename) {
  ENGINE.eval(new java.io.FileReader(filename));
}

function exec() {
  var args = new java.util.ArrayList(arguments.length);
  for (i=0; i < arguments.length; i++)
    args.add(arguments[i]);
  var builder = new java.lang.ProcessBuilder(args);
  var process = builder.start();
  var procIn = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
  while ((lineIn = procIn.readLine()) != null)
    println(lineIn);
  process.waitFor();
  return process.exitValue();
}

