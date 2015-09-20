# Simple Peer to Peer File Sharing System

To run the project you need [ant](https://ant.apache.org/) and java in a linux environment. If you don't want to create Jar files, you can use the script [run-non-jar.sh](https://github.com/gmendonca/simple-p2p-file-sharing/blob/master/run-non-jar.sh).

First you need to run the [script](https://github.com/gmendonca/simple-p2p-file-sharing/blob/master/run.sh) (has to be done with source):

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
* [bench_lookup](https://github.com/gmendonca/simple-p2p-file-sharing#bench_lookup) - Benchmark the system.
* [bench_single_registry](https://github.com/gmendonca/simple-p2p-file-sharing#bench_single_registry) - Benchmark the system.
* [bench_registry](https://github.com/gmendonca/simple-p2p-file-sharing#bench_registry) - Benchmark the system.
* [bench_download](https://github.com/gmendonca/simple-p2p-file-sharing#bench_download) - Benchmark the system.
* [create_directory](https://github.com/gmendonca/simple-p2p-file-sharing#create_directory) - Create a test directory.


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

### bench_lookup

For the Benchmarking of sending Lookup requests to the server, you can use like this:

1. Uses the Central Indexing Server at localhost:3434
    ```sh
    bench_lookup numNodes folderName fileName numRequests
    ```
2. Uses the Central Indexing Server address defined by the user:
    ```sh
    bench_lookup numNodes folderName fileName numRequests serverAddress serverPort
    ```

- numNodes: number of nodes
- folderName: the benchmarking is a peer so needs a folder name to register to the Server
- fileName: the file you want to search in the Server
- numRequests: number of requests per node

### bench_single_registry

### bench_registry

### bench_download

### create_directory

### help
