#!/bin/bash

compile()
{
    javac -d bin/ src/server/*.java src/client/*.java src/bench/*.java src/util/*.java
}


run_server()
{
    cd bin/
    java server/CentralIndexingServer
}

run_client()
{
    DIRECTORY=$1
    PORT=$2
    cd bin/
    java client/Client $DIRECTORY $PORT
}

benchmarking()
{
    N=$1
    PORT=13000
    FOLDERNAME=$2
    FILENAME=$3
    NUMREQUESTS=$4

    cd bin/
    for ((i=0; i<N; i++)); do
        java bench/Benchmarking $FOLDERNAME $(($PORT+$i)) $FILENAME $NUMREQUESTS &
    done
}

help(){
    if [ $# -eq 0 ]; then
        echo "        "
        echo "        run_server - Run the Central Indexing Server"
        echo "        run_client - Run a Peer"
        echo "        benchmarking - Run a Benchmarking with multiple requests to the server"
        echo "        Type 'help command' to know more about each one"
        echo "        "
    elif [ $1 = "run_server" ]; then
            echo "        "
            echo "        Run a Central Indexing Server at port 3434"
            echo "        "
    elif [ $1 = "run_client" ]; then
            echo "        "
            echo "        Run a Peer"
            echo "        run_client directory port"
            echo "        "
    elif [ $1 = "benchmarking" ]; then
            echo "        "
            echo "        Run a Benchmarking with multiple requests to the server"
            echo "        benchmarking numNodes folderName fileName numRequests"
            echo "        "
            echo "        - numNodes: number of nodes"
            echo "        - folderName: the benchmarking is a peer so needs a folder name to register to the Server"
            echo "        - fileName: the file you want to search in the Server"
            echo "        - numRequests: number of requests per node"
            echo "        "
    fi
}

#java bench/Benchmarking peer1 4001 nano & java bench/Benchmarking peer2 4002 nano & java bench/Benchmarking peer2 4004 nano
