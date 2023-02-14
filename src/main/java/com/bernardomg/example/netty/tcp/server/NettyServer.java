
package com.bernardomg.example.netty.tcp.server;

import com.bernardomg.example.netty.tcp.server.channel.NettyChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
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

    private final EventLoopGroup bossLoopGroup   = new NioEventLoopGroup();

    private final ChannelGroup   channelGroup    = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

    public NettyServer() {
        super();
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
        final ServerBootstrap bootstrap;
        final ChannelFuture   channelFuture;

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossLoopGroup, workerLoopGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.AUTO_CLOSE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true);

        bootstrap.childHandler(new NettyChannelInitializer());

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
