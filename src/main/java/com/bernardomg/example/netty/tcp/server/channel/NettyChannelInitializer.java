
package com.bernardomg.example.netty.tcp.server.channel;

import com.bernardomg.example.netty.tcp.server.codec.NettyByteToMessageDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public final class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Integer            availableProcessors;

    private final EventExecutorGroup executors;

    public NettyChannelInitializer() {
        super();

        availableProcessors = Runtime.getRuntime()
            .availableProcessors();
        executors = new DefaultEventExecutorGroup(availableProcessors);
    }

    @Override
    protected final void initChannel(final SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline;

        pipeline = ch.pipeline();

        pipeline.addLast("decoder", new NettyByteToMessageDecoder());
        pipeline.addLast(executors, "handler", new NettySimpleChannelInboundHandler());

    }

}
