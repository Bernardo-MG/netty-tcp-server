
package com.bernardomg.example.netty.tcp.server;

/**
 * Represents a server, and allows starting and stopping it.
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
