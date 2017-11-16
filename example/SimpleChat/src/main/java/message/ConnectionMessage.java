/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import net.socket.ServerSocket;

/**
 *  The connection message is used as a callback when the server endpoint receives a new connection.
 *
 * @author Nicolas Dilley
 */
public interface ConnectionMessage {

    /**
     * A callback containing the actions that a newly connected socket to the server should perfoms.
     *
     * @param socket The newly connected socket.
     */
    void onConnection(ServerSocket socket);
}
