
package com.bernardomg.example.netty.tcp.server.channel;

import java.io.PrintWriter;

import com.bernardomg.example.netty.tcp.server.codec.NettyByteToMessageDecoder;
import com.bernardomg.example.netty.tcp.server.model.Message;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final String      response;

    private final PrintWriter writer;

    public NettyChannelInitializer(final String resp, final PrintWriter wrtr) {
        super();

        response = resp;
        writer = wrtr;
    }

    @Override
    protected final void initChannel(final SocketChannel ch) throws Exception {
        final ChannelPipeline                      pipeline;
        final EventExecutorGroup                   executors;
        final SimpleChannelInboundHandler<Message> inboundHandler;
        final Integer                              availableProcessors;

        log.debug("Initializing channel");

        pipeline = ch.pipeline();

        pipeline.addLast("decoder", new NettyByteToMessageDecoder());

        availableProcessors = Runtime.getRuntime()
            .availableProcessors();

        executors = new DefaultEventExecutorGroup(availableProcessors);
        inboundHandler = new NettySimpleChannelInboundHandler(response, writer);
        pipeline.addLast(executors, "handler", inboundHandler);

        log.debug("Initialized channel");
    }

}
