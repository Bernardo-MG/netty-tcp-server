
package com.bernardomg.example.netty.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class MessagePipelineFactory implements PipelineFactory {

    private final int                availableProcessors;

    private final EventExecutorGroup executors;

    /**
     * Constructor fott {@link MessagePipelineFactory}
     */
    public MessagePipelineFactory() {
        availableProcessors = Runtime.getRuntime()
            .availableProcessors();
        executors = new DefaultEventExecutorGroup(availableProcessors);
    }

    /**
     * Pipeline Factory method for channel initialization
     */
    @Override
    public ChannelInitializer<SocketChannel> createInitializer() {

        return new ChannelInitializer<>() {

            @Override
            protected void initChannel(final SocketChannel ch) throws Exception {
                // Create chanel pipeline
                final ChannelPipeline pipeline = ch.pipeline();

                final MessageDecoder  decoder  = new MessageDecoder();

                pipeline.addLast("decoder", decoder);

                final MessageHandler handler = new MessageHandler();

                pipeline.addLast(executors, "handler", handler);

            }

        };
    }

}
