# J-Socket
A library for java TCP socket communications inspired by socket.io. 

## Features 

- J-Socket is an extremely lightweight open-source library which can be used for commercial use.
- J-Socket provides you with the ability to easily create a bi-directionnal communication between a server and its clients.
- J-Socket does not require a web container to work.
- J-Socket is based on the TCP protocol and is, therefore, reliable.

## Installation

Clone the src/main/java/ folder into your project and start using J-Socket. 
J-Socket requires javax.json to work.

add the following dependency to maven.
```
<dependencies>
        <dependency>
            <groupId>org.glassfish</groupId>
            <artifactId>javax.json</artifactId>
            <version>1.0.4</version>
        </dependency>
 </dependencies>
 ```
 And you should be ready to go.
 
 ## Guide
 
 A simpleChat example can be found under the /example folder. SimpleChat is a basic command line chat app. 
 
 To create a client only a few lines of code are needed : 
 ```
 Socket socket = new Socket("localhost",3030); 
 
 socket.on("message",(Message message) -> // register an event
      System.out.println("I am a client and I have received my message back from the server " + message.getProperty("content"));
 );
 
 socket.on("new message",(Message message) -> 
      System.out.println("another user has sent a message : " + message.getProperty("content");
 }
 
 socket.listen();
 
 Message messageToSend = new Message();
 messageToSend.addProperty("content","Hello server!");
 socket.emit("message",messageToSend);
 ```
 
 To create a server that echo messages received, here is what you need :
 
 ```
 int port = 3030;
 IO echoServer = new IO(port);
 
 echoServer.on("connection",(Socket socket) -> {
      
      System.out.println("A new socket has connected to the server");
      
      socket.on("message",(Message message) -> { // register an event
           System.out.println("A new message has been received by the server");
           socket.emit("message",message);
           socket.broadcast("new message",message);
      })
 });
 ```
 
 ## License
 MIT
