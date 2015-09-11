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

run_a_client()
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

#java bench/Benchmarking peer1 4001 nano & java bench/Benchmarking peer2 4002 nano & java bench/Benchmarking peer2 4004 nano
