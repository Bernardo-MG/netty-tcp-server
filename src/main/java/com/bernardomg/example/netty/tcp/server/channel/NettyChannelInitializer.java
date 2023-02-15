
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

    private final Integer                              availableProcessors;

    private final EventExecutorGroup                   executors;

    private final SimpleChannelInboundHandler<Message> inboundHandler;

    public NettyChannelInitializer(final String response, final PrintWriter writer) {
        super();

        availableProcessors = Runtime.getRuntime()
            .availableProcessors();
        executors = new DefaultEventExecutorGroup(availableProcessors);
        inboundHandler = new NettySimpleChannelInboundHandler(response, writer);
    }

    @Override
    protected final void initChannel(final SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline;

        log.debug("Initializing channel");

        pipeline = ch.pipeline();

        pipeline.addLast("decoder", new NettyByteToMessageDecoder());
        pipeline.addLast(executors, "handler", inboundHandler);
    }

}
