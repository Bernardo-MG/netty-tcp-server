
package com.bernardomg.example.netty.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NettyServer implements Server {

    private final EventLoopGroup                   bossLoopGroup;

    private final ChannelGroup                     channelGroup;

    private final Class<? extends PipelineFactory> pipelineFactoryClass;

    private final EventLoopGroup                   workerLoopGroup;

    /**
     * Initialize the netty server class
     *
     * @param pipelineFactoryType
     *            {@link Class} of the piprline factory type
     */
    public NettyServer(final Class<? extends PipelineFactory> pipelineFactoryType) {
        // Initialization private members

        bossLoopGroup = new NioEventLoopGroup();

        workerLoopGroup = new NioEventLoopGroup();

        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        pipelineFactoryClass = pipelineFactoryType;
    }

    /**
     * Shutdown the server
     *
     * @throws Exception
     */
    @Override
    public final void shutdown() throws Exception {
        channelGroup.close();
        bossLoopGroup.shutdownGracefully();
        workerLoopGroup.shutdownGracefully();
    }

    /**
     * Startup the TCP server
     *
     * @param port
     *            port of the server
     * @throws Exception
     *             if any {@link Exception}
     */
    @Override
    public final void startup(final int port) throws Exception {
        final ServerBootstrap    bootstrap;
        final ChannelFuture      channelFuture;
        @SuppressWarnings("rawtypes")
        final ChannelInitializer initializer;
        final PipelineFactory    pipelineFactory;

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossLoopGroup, workerLoopGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.AUTO_CLOSE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true);

        pipelineFactory = pipelineFactoryClass.newInstance();

        initializer = pipelineFactory.createInitializer();

        bootstrap.childHandler(initializer);

        try {
            log.debug("Binding port {}", port);
            channelFuture = bootstrap.bind(port)
                .sync();
            channelGroup.add(channelFuture.channel());
            log.debug("Finished startup");
        } catch (final Exception e) {
            log.error(e.getLocalizedMessage(), e);
            shutdown();
            throw e;
        }
    }

}
