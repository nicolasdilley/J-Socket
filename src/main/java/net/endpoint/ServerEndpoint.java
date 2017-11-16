/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endpoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import message.ConnectionMessage;
import room.Room;
import net.socket.SocketThread;
/**
 * The server endpoint is a wrapper around the javax.net.ServerSocket.
 *
 * The endpoint accepts incoming connection, add them to the default room and attach the connection message to it.
 * @author Nicolas Dilley
 */
public class ServerEndpoint extends Endpoint{

    private ArrayList<Room> rooms;
    private java.net.ServerSocket socket;
    private ConnectionMessage connectionMessage;
    private Room defaultRoom;
    /**
     * The server endpoint needs a port number on which to listen to and a connection message describing how to handle
     * new connection and how to responds to them.
     *
     * @param portNumber the port on which the server should listen to.
     * @param connectionMessage the callback to call when a new socket is connecting to the server.
     * @throws UnknownHostException
     * @throws IOException
     */
    public ServerEndpoint(int portNumber, ConnectionMessage connectionMessage) throws UnknownHostException, IOException
    {
        super(portNumber);
        rooms = new ArrayList<>();
        this.connectionMessage = connectionMessage;
        socket = new java.net.ServerSocket(portNumber);

        addDefaultRoom();
    }

    private void addDefaultRoom()
    {
        defaultRoom = new Room(Room.DEFAULT_ROOM_NAME);
        rooms.add(defaultRoom);
    }
    public void connectToRoom(net.socket.ServerSocket socket,String roomName)
    {
        // find the room
        boolean found = false;
        int i = 0;


        while(!found && i < rooms.size())
        {
            found = rooms.get(i).getName().equals(roomName);
            i++;
        }

        if(found) // The room already exist, therefore add the client to it
        {
                // the user is already in a room.
                //disconnect the user from his current room
                socket.getRoom().removeUser(socket);
                Room room = rooms.get(i-1);
                room.addUser(socket);
        }
        else // the room does not exist, create a new room and add client to it
        {
            Room room = new Room(roomName);
            room.addUser(socket);
            socket.setRoom(room);
            rooms.add(room);
        }
    }

    @Override
    public void listen()
    {
        this.setListening(true);

        while(isListening())
        {

            try {
                Socket clientSocket = this.socket.accept();

                net.socket.ServerSocket sock = new net.socket.ServerSocket(this.getPort(),defaultRoom);
                sock.getClientEndpoint().setSocket(clientSocket);
                sock.getClientEndpoint().setListening(true);


                connectionMessage.onConnection(sock);

                Thread thread = new Thread(new SocketThread(sock));
                thread.start();
            }
            catch(Exception e)
            {
                System.err.println(e);
            }

        }
    }
}
