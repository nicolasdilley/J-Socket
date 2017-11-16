
package net.endpoint;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import message.MessageHandler;


/**
 *  Endpoint is used to do the "real" socket messaging.
 *  The socket will listen to incoming messages
 * @author Nicolas Dilley
 */
public abstract class Endpoint {

    private HashMap<String,MessageHandler> events; // holds all  the event that the endpoint has to respond to.
    protected PrintWriter out;
    protected BufferedReader in;
    private boolean listening;
    private int portNumber;


    /**
     * The endpoint is a wrapper around a javax.net.Socket or ServerSocket depending on the child inheriting it.
     *
     * @param portNumber the port to listens or send to.
     * @throws UnknownHostException
     * @throws IOException
     */
    public Endpoint(int portNumber) throws UnknownHostException, IOException
    {
        this.portNumber = portNumber;
        events = new HashMap<>();
    }

    public int getPort()
    {
        return portNumber;
    }
    public abstract void listen();

    public void addEvent(String messageType,MessageHandler handler)
    {
        events.put(messageType, handler);
    }

    public MessageHandler getHandler(String message)
    {
        return events.get(message);
    }

    public void setListening(boolean listening)
    {
        this.listening = listening;
    }

    public boolean isListening()
    {
        return listening;
    }
    public void disconnect()
    {
        listening = false;
    }


    public BufferedReader getReader()
    {
        return in;
    }

    public PrintWriter getWriter()
    {
        return out;
    }
}
