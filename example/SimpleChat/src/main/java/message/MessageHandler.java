/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import net.socket.Socket;

/**
 * The interface Message Handler is used has a callback when a particular message type is received from the other end.
 *
 * @author Nicolas Dilley
 */
public interface MessageHandler {

    /**
     * onMessage gets called when a particular message type has been received by the socket.
     *
     * @param data the data received by the socket
     * @param socket the socket that sent the
     */
    void onMessage(Message data); // put what to do when a message has been received in this method.
}
