# pi-rpc

The main objective of this project is to study remote procedure calls systems using Scala, Finagle and Apache Thrift. Finagle is a RPC system for the JVM, used to construct high-concurrency servers (protocol agnostic) and Apache Thrift is an interface definition language and binary communication protocol used to create cross-language services that have a great perfomance when compared to Rest and Soap services.

The pi-rpc system task is to calculate the pi number using the Bailey–Borwein–Plouffe formula. 

<img src="https://drive.google.com/uc?export=view&id=1wvDiP52a_4aAqy5GOrgNe9q9cTOI5GOW" title="Bailey–Borwein–Plouffe"/>

The server is responsible for hold the current pi number aproximation while distributes tasks to the clients and updates the pi number with the client results.