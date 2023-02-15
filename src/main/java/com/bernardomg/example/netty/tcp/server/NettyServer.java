
package com.bernardomg.example.netty.tcp.server;

import java.io.PrintWriter;
import java.util.Objects;

import com.bernardomg.example.netty.tcp.server.channel.NettyChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NettyServer implements Server {

    private final EventLoopGroup bossLoopGroup   = new NioEventLoopGroup();

    private final ChannelGroup   channelGroup    = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final Integer        port;

    private final String         response;

    private final EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

    private final PrintWriter    writer;

    public NettyServer(final Integer prt, final String resp, final PrintWriter writ) {
        super();

        port = Objects.requireNonNull(prt);
        response = Objects.requireNonNull(resp);
        writer = Objects.requireNonNull(writ);
    }

    @Override
    public final void start() {
        final ServerBootstrap bootstrap;
        final ChannelFuture   channelFuture;

        log.trace("Starting server");

        // Activate Log4j logger factory
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        bootstrap = new ServerBootstrap();
        bootstrap
            // Registers groups
            .group(bossLoopGroup, workerLoopGroup)
            // Defines channel
            .channel(NioServerSocketChannel.class)
            // Adds logging
            .handler(new LoggingHandler(LogLevel.INFO))
            // Configuration
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.AUTO_CLOSE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            // Child handler
            .childHandler(new NettyChannelInitializer(response, writer));

        try {
            log.debug("Binding port {}", port);
            channelFuture = bootstrap.bind(port)
                .sync();
        } catch (final InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
            stop();

            // Rethrows exception
            throw new RuntimeException(e);
        }

        channelGroup.add(channelFuture.channel());

        log.trace("Started server");
    }

    @Override
    public final void stop() {
        log.trace("Stopping server");

        channelGroup.close();
        bossLoopGroup.shutdownGracefully();
        workerLoopGroup.shutdownGracefully();

        log.trace("Stopped server");
    }

}
