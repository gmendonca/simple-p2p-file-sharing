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

A quickly overview of the files are shown above and further the full explanation of each one:

* compile - Compile using ant and build the jar files to run the rest of the script.
* run_server - Run the Central Indexing Server at the Default port 3434.
* run_client - Run a Peer.
* benchmarking - Benchmark the system.


## compile

- There aren't possible option here, but this will only work with a working version of Apache Ant.
## run_server

- By default it runs the server at the port 3434, to change it you have to use:

```sh
$ run_server portNumber
```
### run_client

### benchmarking
