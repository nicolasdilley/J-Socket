/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.socket;

import message.MessageHandler;
import message.Message;
import net.endpoint.ClientEndpoint;
import room.Room;


/**
 * The socket is used to emit and react to certain message type sent by the other end.
 * Calling on() will register the MessageHandler and be called when the messageType gets received by the socket.
 * Calling emit() will send a Message to the other end and specify, with the message type, what message handler to invoke on the other end.
 *
 * @author Nicolas Dilley
 */
public class Socket {

    private ClientEndpoint endpoint; // The endpoint to talk to.
    private int port; // the port that the socket will listen to
    private String roomName = Room.DEFAULT_ROOM_NAME; // when a client socket it is in the default room
    private String url; // the url of the server socket.

    /**
     * Creates a socket and bind it to the url and the port.
     *
     * @param url the url of the server to talk to
     * @param port the port on which the server listens for incoming requests.
     */
    public Socket(String url,int port)
    {
        this.url = url;
        this.port = port;

        try
        {
            endpoint = new ClientEndpoint(url,port);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

    }

    public Socket(int port)
    {
        this.port = port;
        try
        {
            endpoint = new ClientEndpoint(port);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    /**
     * Set the client endpoint of the socket.
     * @param endpoint the new client endpoint.
     */
    public void setClientEndpoint(ClientEndpoint endpoint)
    {
        this.endpoint = endpoint;
    }

    /**
     * return the underlying client endpoint linked to this socket.
     *
     * @return the client endpoint of the socket.
     */
    public ClientEndpoint getClientEndpoint()
    {
        return endpoint;
    }

    /**
     * makes the socket listen to incoming requests.
     *
     * The socket listens in a separate thread.
      */
    public void listen()
    {
        endpoint.listen();
    }
    /**
     * returns the port number on which the socket listens
     * @return int the port number
     */
    public int getPort()
    {
        return port;
    }
    /**
     * this is the workhorse of the Socket class.
     * on is used to register an event to the socket.
     * MessageType is the name of the event to listen to.
     * handler is the callback.
     *
     * @param messageType // the event to listen to.
     * @param handler   // the callback to call when the event is fired.
     */
    public void on(String messageType,MessageHandler handler)
    {
        // register the event to the server
        endpoint.addEvent(messageType,handler);
    }

    /**
     * Send data back to through the socket to the client or server.
     *
     * @param messageType // the type of the message
     * @param message // the message to send back.
     */
    public void emit(String messageType, Message message)
    {
        message.setHeader(messageType);

        try
        {
            String toSend = message.getMessageInJson();
            this.endpoint.getWriter().println(toSend);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    /**
     * To call when the socket wants to close the connection with the other end.
     */
    public void disconnect()
    {
        endpoint.disconnect();
    }

}
