#!/bin/bash

compile()
{
    javac server/*.java client/*.java bench/*.java util/*.java
}


run_server()
{
    java server/CentralIndexingServer
}

run_a_client()
{
    DIRECTORY=$1
    PORT=$2
    java client/Client $DIRECTORY $PORT
}

benchmarking()
{
    N=$1
    PORT=13000
    FILENAME=$2
    NUMREQUESTS=$3

    for ((i=0; i<=N; i++)); do
        java bench/Benchmarking peer1/ $(($PORT+$i)) $FILENAME $NUMREQUESTS &
    done
}

#java bench/Benchmarking peer1 4001 nano & java bench/Benchmarking peer2 4002 nano & java bench/Benchmarking peer2 4004 nano
