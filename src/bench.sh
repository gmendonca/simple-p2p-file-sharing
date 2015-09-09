#javac server/*.java client/*.java bench/*.java util/*.java

#java server/CentralIndexingServer
#java client/Client peer3/ 4003

java bench/Benchmarking peer1 4001 nano & java bench/Benchmarking peer2 4002 nano & java bench/Benchmarking peer2 4004 nano
