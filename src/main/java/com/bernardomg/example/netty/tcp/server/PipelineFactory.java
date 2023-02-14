
package com.bernardomg.example.netty.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public interface PipelineFactory {

    // Socket Channel initializer
    ChannelInitializer<SocketChannel> createInitializer();

}
