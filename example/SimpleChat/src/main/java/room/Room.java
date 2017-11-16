package room;

import java.util.ArrayList;
import message.Message;
import net.socket.ServerSocket;

/**
 *  A room is a way to group certain users together.
 *  Room can be extended to suit needs.
 *
 * @author Nicolas Dilley
 */
public class Room {

    private String name;
    private ArrayList<ServerSocket> sockets;
    public static String DEFAULT_ROOM_NAME = "GENERAL";
    public Room(String name)
    {
        this.name = name;
        sockets = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<ServerSocket> getSockets()
    {
        return sockets;
    }

    public void removeUser(ServerSocket socket)
    {
        if(!sockets.remove(socket))
        {
            System.err.println("Error : The user is not contained in the room.");
        }
    }
    public void addUser(ServerSocket socket)
    {
        sockets.add(socket);
    }

    public void broadcast(String messageType, Message message,ServerSocket sender)
    {
        for(ServerSocket socket : sockets)
        {
            if(socket != sender) {
                socket.emit(messageType, message);
            }
        }
    }
}
