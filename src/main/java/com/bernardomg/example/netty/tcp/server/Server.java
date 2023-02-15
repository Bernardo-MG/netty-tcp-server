
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
     *
     * @throws Exception
     *             if any {@link Exception}
     */
    public void start();

    /**
     * Stops the server.
     *
     * @throws Exception
     */
    public void stop();

}
