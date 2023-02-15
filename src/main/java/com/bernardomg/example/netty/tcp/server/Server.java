
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
     * @throws Exception
     *             if any {@link Exception}
     */
    public void startup() throws Exception;

}
