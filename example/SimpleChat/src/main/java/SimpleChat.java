
import java.util.HashMap;
import java.util.Scanner;
import message.Message;
import net.IO;
import net.socket.ServerSocket;
import net.socket.Socket;
/**
 *
 * @author Nicolas Dilley
 */
public class SimpleChat {



    public static void main(String[] args) {


        System.out.println("To create a client type 'client' otherwise it will create a server.");

        Scanner scanner = new Scanner(System.in);

        String appType = scanner.nextLine();

        if(appType.equals("client"))
        {
            System.out.println("What is your name ?");

            String username = scanner.nextLine();

            System.out.println("Hello " + username + " ,to exit type 'disconnect'");

            Socket socket = new Socket("localhost",3030); // create the client socket


            Message usernameMessage = new Message(); // the message that will contain the username of the client
            usernameMessage.addProperty("username",username);

            socket.emit("username" , usernameMessage); // send the username to the server

            // -- ATTACH THE LISTENER TO THE SOCKETS

            socket.on("message", (Message message) -> { // when the server emit "message"
                System.out.println(message.getProperty("message")); // print the message received to the console.
            });

            socket.on("new user" ,(Message message) -> { // when the server emit "new user"
                System.out.println(message.getProperty("username") + "has just logged in"); // print new user
            });

            // ------

            socket.listen(); // make the socket listen to incoming messages.


            // === SENDING MESSAGE TO THE SERVER

            String messageToSend = "";

            System.out.print("Type your message > ");

            while((messageToSend = scanner.nextLine()) != null) {

                if(messageToSend.equals("disconnect"))
                {
                    Message message = new Message();
                    message.addProperty("test","test");
                    socket.emit("disconnect",message);
                    System.exit(0); // log out
                }
                else {
                    Message message = new Message();
                    message.addProperty("message", messageToSend);

                    socket.emit("message", message);
                }
            }

            //
        }
        else
        {

            HashMap<ServerSocket,String> usernames = new HashMap<>(); // the server needs to keep a collection of the usernames.


            //create a server
            IO io = new IO(3030);



            io.on("connection", (ServerSocket socket) -> { // register events when a client connects to the server.


                socket.on("username", (Message message) -> {
                    System.out.println(message.getProperty("username") + " is new connected to the server ! ");
                    usernames.put(socket,message.getProperty("username"));

                    Message newUserMessage = new Message();
                    newUserMessage.addProperty("username", message.getProperty("username"));
                    socket.broadcast("new user" , newUserMessage);

                });
                socket.on("message", (Message message) -> {

                    System.out.println("SERVER: I have received a message : " + message.getProperty("message"));

                    Message conn = new Message();

                    conn.addProperty("message",usernames.get(socket) + " :: " + message.getProperty("message"));

                    socket.broadcast("message",conn);
                });

                socket.on("disconnect", (Message message) -> {
                        System.out.println("A user wants to disconnect");
                        socket.getRoom().removeUser(socket); // remove the user of his room.
                        socket.disconnect();
                });
            });


        }
    }

}
