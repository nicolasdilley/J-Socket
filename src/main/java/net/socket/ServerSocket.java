package net.socket;

import message.Message;
import room.Room;

/**
 * Server socket are the sockets used on the server only.
 * They add functionality of broadcasting to their current room.
 *
 */
public class ServerSocket extends Socket{

    public Room room; // the current room the socket is in.

    public ServerSocket(int port,Room room)
    {
        super(port);
        this.room = room;
        this.room.addUser(this);
    }

    public Room getRoom()
    {
        return this.room;
    }

    public void setRoom(Room room)
    {
        this.room = room;
    }

    /**
     * Broadcast sends a message to all the users connected to the same room as the socket.
     *
     * @param messageType the message type that triggers the handler.
     * @param handler the handler to trigger when a message with a header of message type has been received.
     */
    public void broadcast(String messageType, Message handler)
    {
        room.broadcast(messageType,handler,this);
    }

}
