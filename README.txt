Airports - assessment for Lunatech
by vladimirbodnar@yahoo.fr

Java 8 is required.

The PATH must contain winutils binary, to do so download the binary and add it to PATH.
For windows download winutils.exe and libwinutils.lib:
- 64bits: https://github.com/steveloughran/winutils/tree/master/hadoop-2.6.0/bin
- 32bits: https://code.google.com/p/rrd-hadoop-win32/source/checkout

In case of "java.io.IOException: Could not locate executable null\bin\winutils.exe"
Specify hadoop home by property, for ex: -Dhadoop.home.dir=C:\tools\hadoop

To start the server, set JAVA_HOME then navigate to bin folder and run the starter script.

Specify the application port with -Dhttp.port, default port is 9000.

For example:

> airports.bat -Dhttp.port=9001

Then in your browser got to http://localhost:9001

The home page contains links to Query and Reports pages.

The Query page enables to user to get airports with runways by country name.

The Reports page contains:
- Top 10 countries with most airports
- Top 10 countries with less airports
- Surface types by country