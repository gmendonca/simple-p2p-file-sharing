#!/bin/bash

compile()
{
    ant clean
    ant compile
    ant jar
}

run_server()
{
    PORT=$1

    if [ $# -eq 1 ]; then
        java -jar -classpath build/ CentralIndexingServer.jar $PORT
    else
        java -jar -classpath build/ CentralIndexingServer.jar
    fi
}

run_client()
{
    DIRECTORY=$1
    PORT=$2
    SERVER=$3
    SERVERPORT=$4

    if [ $# -eq 2 ]; then
        java -jar -classpath build/ Client.jar $DIRECTORY $PORT
    elif [ $# -eq 4 ]; then
        java -jar -classpath build/ Client.jar $DIRECTORY $PORT $SERVER $SERVERPORT
    else
        echo "It should be run_client directory port"
    fi
}

bench_lookup()
{
    N=$1
    PORT=13000
    FOLDERNAME=$2
    FILENAME=$3
    NUMREQUESTS=$4
    SERVER=$5
    SERVERPORT=$6

    if [ $# -eq 4 ]; then
        for ((i=0; i<N; i++)); do
            java -jar -classpath build/ BenchLookup.jar $FOLDERNAME $(($PORT+$i)) $FILENAME $NUMREQUESTS &
        done
    elif [ $# -eq 6 ]; then
        for ((i=0; i<N; i++)); do
            java -jar -classpath build/ BenchLookup.jar $FOLDERNAME $(($PORT+$i)) $FILENAME $NUMREQUESTS $SERVER $SERVERPORT &
        done
    else
        echo "It should be bench_lookup numNodes folderName fileName numRequests"
    fi

}

bench_download()
{
    N=$1
    PORT=13000
    FOLDERNAME=$2
    FILENAME=$3
    NUMREQUESTS=$4
    SERVER=$5
    SERVERPORT=$6

    if [ $# -eq 6 ]; then
        for ((i=0; i<N; i++)); do
            java -jar -classpath build/ BenchDownloadjar $FOLDERNAME $(($PORT+$i)) $FILENAME $NUMREQUESTS $SERVER $SERVERPORT &
        done
    elif [ $# -eq 4 ]; then
        for ((i=0; i<N; i++)); do
            java -jar -classpath build/ BenchDownload.jar $FOLDERNAME $(($PORT+$i)) $FILENAME $NUMREQUESTS &
        done
    else
        echo "It should be bench_download numNodes folderName fileName numRequests"
    fi

}

bench_single_registry(){

    N=$1
    PORT=13000
    FOLDERNAME=$2

    if [ $# -eq 4 ]; then
        for ((i=0; i<N; i++)); do
            java -jar -classpath build/ BenchSingleRegistry.jar $FOLDERNAME $(($PORT+$i)) $SERVER $SERVERPORT &
        done
    elif [ $# -eq 2 ]; then
        for ((i=0; i<N; i++)); do
            java -jar -classpath build/ BenchSingleRegistry.jar $FOLDERNAME $(($PORT+$i)) &
        done
    else
        echo "It should be bench_single_registry numPeers folderName"
    fi
}

bench_registry(){

    PORT=13000
    FOLDERNAME=$1
    NUMPEERS=$2

    if [ $# -eq 4 ]; then
        java -jar -classpath build/ BenchRegistry.jar $FOLDERNAME $(($PORT+$i)) $NUMPEERS $SERVER $SERVERPORT &
    elif [ $# -eq 2 ]; then
        java -jar -classpath build/ BenchRegistry.jar $FOLDERNAME $(($PORT+$i)) $NUMPEERS &
    else
        echo "It should be bench_registry folderName numPeers"
    fi
}

create_directory(){
    FOLDERNAME=$1
    N=$2

    mkdir $FOLDERNAME
    if [ $# -eq 2 ]; then
        for ((i=0; i<N; i++)); do
            if [ $i -gt 9 ]; then
                base64 /dev/urandom | head -c $((RANDOM%20000+1000)) > $FOLDERNAME/file-0$i
            elif [ $i -gt 99 ]; then
                base64 /dev/urandom | head -c $((RANDOM%20000+1000)) > $FOLDERNAME/file-$i
            else
                base64 /dev/urandom | head -c $((RANDOM%20000+1000)) > $FOLDERNAME/file-00$i
            fi
        done
    else
        echo "It should be create_directory folderName numFiles"
    fi
}

help(){
    if [ $# -eq 0 ]; then
        echo "        "
        echo "        run_server - Run the Central Indexing Server"
        echo "        run_client - Run a Peer"
        echo "        bench_lookup - Run a Benchmarking with multiple requests to the server"
        echo "        bench_single_registry - Run a Benchmarking registering multiple peers creating multipe instances"
        echo "        bench_registry - Run a Benchmarking registering multiple peers with a single instance of the program"
        echo "        bench_download - Run a Benchmarking with multiple downloads to other peers"
        echo "        create_directory - Create directory with text files for testing the system"
        echo "        Type 'help command' to know more about each one"
        echo "        "
    elif [ $1 = "run_server" ]; then
            echo "        "
            echo "        Run a Central Indexing Server at port 3434"
            echo "        "
            echo "        or"
            echo "        "
            echo "        run_server port"
            echo "        "
    elif [ $1 = "run_client" ]; then
            echo "        "
            echo "        Run a Peer"
            echo "        run_client directory port"
            echo "        "
            echo "        or"
            echo "        "
            echo "        run_client directory port serverAddress serverPort"
            echo "        "
    elif [ $1 = "bench_lookup" ]; then
            echo "        "
            echo "        Run a Benchmarking with multiple requests to the server"
            echo "        bench_lookup numNodes folderName fileName numRequests"
            echo "        "
            echo "        - numNodes: number of nodes"
            echo "        - folderName: the benchmarking is a peer so needs a folder name to register to the Server"
            echo "        - fileName: the file you want to search in the Server"
            echo "        - numRequests: number of requests per node"
            echo "        "
            echo "        or"
            echo "        "
            echo "        bench_lookup numNodes folderName fileName numRequests serverAddress serverPort"
            echo "        "
    elif [ $1 = "bench_single_registry" ]; then
            echo "        "
            echo "        Run a Benchmarking registering multiple peers creating multipe instances"
            echo "        bench_single_registry numPeers folderName"
            echo "        "
            echo "        - numPeers: number of Peers rgistering"
            echo "        - folderName: the benchmarking is a peer so needs a folder name to register to the Server"
            echo "        "
            echo "        or"
            echo "        "
            echo "        bench_single_registry numPeers folderName serverAddress serverPort"
            echo "        "
    elif [ $1 = "bench_registry" ]; then
            echo "        "
            echo "        Run a Benchmarking registering multiple peers with a single instance of the program"
            echo "        bench_registry folderName numPeers"
            echo "        "
            echo "        - folderName: the benchmarking is a peer so needs a folder name to register to the Server"
            echo "        - numPeers: number of Peers rgistering"
            echo "        "
            echo "        or"
            echo "        "
            echo "        bench_registry folderName numPeers serverAddress serverPort"
            echo "        "
    elif [ $1 = "bench_download" ]; then
            echo "        "
            echo "        Run a Benchmarking with multiple downloads to other peers"
            echo "        bench_download numNodes folderName fileName numRequests"
            echo "        "
            echo "        - numNodes: number of nodes"
            echo "        - folderName: the benchmarking is a peer so needs a folder name to register to the Server"
            echo "        - fileName: the file you want to search in the Server"
            echo "        - numRequests: number of requests per node"
            echo "        "
            echo "        or"
            echo "        "
            echo "        bench_download numNodes folderName fileName numRequests serverAddress serverPort"
            echo "        "
    elif [ $1 = "create_directory" ]; then
            echo "        "
            echo "        Create directory with text files for testing the system"
            echo "        create_directory folderName numFiles"
            echo "        "
            echo "        - folderName: Folder name that will be created to put the text files on"
            echo "        - numFiles : number of files that will be created in the created folder"
            echo "        "
    fi
}
