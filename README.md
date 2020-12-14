# test
How to Run in Linux env:

Export PATH  for JAVA_HOME and $JAVA_HOME/bin

cd src
Compile files
javac -cp "../lib/junit.jar:."  -d "../bin" com/fiexch/orderbook/*.java

Run App
java -cp "../lib/junit.jar:../bin" com.fiexch.orderbook.App > ../resources/log.txt

Run Unit tests
java -cp "../lib/junit.jar:../lib/org.hamcrest.core_1.3.0.v20180420-1519.jar:../bin:." org.junit.runner.JUnitCore com.fiexch.orderbook.TestUtils


Assumptions Made:

1. No debug log files created using logger
2. Extensive Exception handling is not considered
3. The FIX Format used for publishing need to append with header, session info and trailer
4. File path configurations are hard coded
5. Performance optimization is not looked at closely
