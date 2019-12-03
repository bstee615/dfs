# Project 2

## To compile:

Maven is required to build the project.
Note that the `publish` directory contains a pre-compiled assembly, `project2.jar` that runs on Pyrite.

Run `mvn clean package` to publish an assembly in `target/project2.jar`.

## To run:

In general, run `java -cp project2.jar org.benjis.project2.<ClassName>` to run from the prepackaged assembly.
You can use `mvn exec:java -Dexec.mainClass=\"org.benjis.project2.<ClassName>\"` to execute with a main class without compiling.
You can use `run.sh` to run with the jar on Pyrite. (for acceptance testing)
You can use `run.bat` to run with `maven-exec` on Windows. (for local testing)

`Stage1Server` is the server program for Stage 1.
It takes two arguments: a hostname and a port to run on.

`FileSystemTest` is a client that allows you to test all the functionality of Stage 1.
It also takes a hostname and a port to run on.

Example of running client/server for Stage 1:

First, copy project2.jar into Pyrite. My instructions have it in `~/project2`.
You can use `publish.bat <username>` to publish to `~/project2` in Pyrite

```
# ssh into Pyrite. Let's say we end up on pyrite-n3.cs.iastate.edu.
cd project2
./run.sh Stage1Server 0.0.0.0 10043
# Or: java -cp project2.jar org.benjis.project2.Stage1Server 0.0.0.0 10043
# Press Ctrl+C when you want to shut it down.
```

```
# Open a new `ssh` into Pyrite
cd project2
# You'll have to replace the hostname with the name of
# whatever machine the server is running on.
./run.sh FileSystemTest pyrite-n3.cs.iastate.edu 10043
# Or: java -cp project2.jar org.benjis.project2.FileSystemTest pyrite-n3.cs.iastate.edu 10043
```
