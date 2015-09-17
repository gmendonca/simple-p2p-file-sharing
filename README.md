# Simple Peer to Peer File Sharing System

To run the project you need [ant](https://ant.apache.org/) and java in a linux environment.

First you need to run the script (has to be done with source):

```sh
$ source run.sh
```

You can use the command help to get help all the possibilities. The possible commands are:

```sh
$ compile
$ run_server
$ run_client
$ benchmarking
```

A quickly overview of the files are shown above:

* [compile](https://github.com/gmendonca/simple-p2p-file-sharing#compile) - Compile using ant and build the jar files to run the rest of the script.
* [run_server](https://github.com/gmendonca/simple-p2p-file-sharing#run_server) - Run the Central Indexing Server at the Default port 3434.
* [run_client](https://github.com/gmendonca/simple-p2p-file-sharing#run_client) - Run a Peer.
* [benchmarking](https://github.com/gmendonca/simple-p2p-file-sharing#benchmarking) - Benchmark the system.


## Commands

### compile

There aren't possible option here, but this will only work with a working version of Apache Ant.

### run_server

By default it runs the server at the port 3434, to change it you have to use:

```sh
$ run_server portNumber
```
### run_client

For the client there are two possibilities, the first one uses the default address for the Central Indexing Server and the second one you have to define the address and port for the server:

1. Uses the Central Indexing Server at localhost:3434
```sh
$ run_client directory port
```

2. Uses the Central Indexing Server address defined by the user:
```sh
run_client directory port serverAddress serverPort
```

### benchmarking
