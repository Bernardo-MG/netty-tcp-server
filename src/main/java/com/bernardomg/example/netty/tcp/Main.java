
package com.bernardomg.example.netty.tcp;

import com.bernardomg.example.netty.tcp.server.MessagePipelineFactory;
import com.bernardomg.example.netty.tcp.server.NettyServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        try {
            new NettyServer(MessagePipelineFactory.class).startup(8080);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // final Integer exitCode;
        //
        // log.error("Error");
        //
        // exitCode = new CommandLine(new TcpServerMenu()).execute(args);
        //
        // log.debug("Exited with code {}", exitCode);
        //
        // System.exit(exitCode);
    }

    public Main() {
        super();
    }

}
