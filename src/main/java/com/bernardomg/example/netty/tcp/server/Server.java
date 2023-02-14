
package com.bernardomg.example.netty.tcp.server;

public interface Server {

    /**
     * Shutdown the server
     *
     * @throws Exception
     */
    public void shutdown() throws Exception;

    /**
     * Startup the TCP server
     *
     * @param port
     *            port of the server
     * @throws Exception
     *             if any {@link Exception}
     */
    public void startup(int port) throws Exception;

}
