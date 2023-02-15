
package com.bernardomg.example.netty.tcp.server;

/**
 * Generic server. Supports starting and stopping it.
 *
 * @author bernardo.martinezg
 *
 */
public interface Server {

    /**
     * Starts the server.
     */
    public void start();

    /**
     * Stops the server.
     */
    public void stop();

}
