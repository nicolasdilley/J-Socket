/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endpoint;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import net.socket.SocketThread;

/**
 *
 * @author Nicolas Dilley
 */
public class ClientEndpoint extends Endpoint{

    private java.net.Socket socket; // the wrapper socket.
    private String url; // the url of the server to which the client will talk to.

    public ClientEndpoint(String url,int port) throws UnknownHostException, IOException
    {
        super(port);
        this.url = url;
        this.setSocket(new Socket(this.url,port));
    }

    public ClientEndpoint(int port) throws UnknownHostException, IOException
    {
        super(port);
    }

    @Override
    public void listen()
    {
        setListening(true);
        net.socket.Socket sock = new net.socket.Socket(this.getPort());
        sock.setClientEndpoint(this);
        Thread thread = new Thread(new SocketThread(sock));
        thread.start();
    }

    public void setSocket(java.net.Socket socket)
    {
        try
        {
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        this.socket = socket;
    }

    public java.net.Socket getSocket()
    {
        return socket;
    }

}
