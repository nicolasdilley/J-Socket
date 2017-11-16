package net;

import java.util.ArrayList;
import message.ConnectionMessage;
import net.endpoint.ServerEndpoint;
import room.Room;
/**
 * IO is a wrapper around a server endpoint.
 * the io has only one method which accepts a message (at the moment it only support "connection") and a
 * ConnectionMessage object which describe how the server should respond to incoming requests from clients.
 *
 * @author Nicolas Dilley
 */
public class IO extends Thread{

    private int port;
    private ServerEndpoint endpoint;
    private ArrayList<Room> rooms;

    /**
     *  The IO only needs the port on which to listen to.
     *
     * @param port
     */
    public IO(int port)
    {
        this.port = port;
        rooms = new ArrayList<>();
    }

    /**
     * on is the main method of the server.
     * the on method will take a message (at the moment only "connection" is supported) and a connection Message
     * which will tell the server how to respond to new connections.
     *
     * @param message has to be "connection"
     * @param conn The conn describes what the server should do to incoming connections.
     */
    public void on(String message, ConnectionMessage conn)
    {
        message = message.toLowerCase();

        switch(message)
        {
            case "connection" :
            {
                try
                {
                    endpoint = new ServerEndpoint(port,conn);
                    endpoint.listen();
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
                break;

            }
            default:
            {
                System.err.println("Error : was expecting 'connection' received " + message);
            }
        }
    }
}
