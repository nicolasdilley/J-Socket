package net.socket;

import message.Message;
import message.MessageHandler;
import net.endpoint.ClientEndpoint;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 *  The socket thread will listen to incoming messages and respond according to the handler
 *  set by the user.
 *
 * @author Nicolas Dilley
 */
public class SocketThread implements Runnable{

    private ClientEndpoint endpoint; // read only
    private BufferedReader reader;
    private PrintWriter writer;


    public SocketThread(Socket socket)
    {
        this.endpoint = socket.getClientEndpoint();
        this.reader = endpoint.getReader();
        this.writer = endpoint.getWriter();
    }

    @Override
    public void run()
    {

        while(endpoint.isListening())
        {
            String input = "";

            // read the message in the buffer
            try {
                while ((input = reader.readLine()) != null) {
                        input.trim();
                    if (!input.equals("")) {

                        Message message = new Message(input);

                        // execute the event handler
                        MessageHandler handler = endpoint.getHandler(message.getHeader());

                        if (handler != null) // check if the user has attached a message handler with the particular header.
                        {
                            handler.onMessage(message);
                        } else {
                            System.err.println("Could not find a message type that correspond to " + message.getHeader());
                        }
                    }
                }

                }
               catch(Exception e)
                {
                    System.err.println(e);
                }
        }
    }

}
