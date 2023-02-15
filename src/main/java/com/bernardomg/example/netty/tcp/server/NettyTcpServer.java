/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.example.netty.tcp.server;

import java.io.PrintWriter;
import java.util.Objects;

import com.bernardomg.example.netty.tcp.server.channel.ResponseListenerChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty based TCP server.
 *
 * @author bernardo.martinezg
 *
 */
@Slf4j
public final class NettyTcpServer implements Server {

    private final EventLoopGroup bossLoopGroup   = new NioEventLoopGroup();

    private final ChannelGroup   channelGroup    = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final Integer        port;

    private final String         response;

    private final EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

    private final PrintWriter    writer;

    public NettyTcpServer(final Integer prt, final String resp, final PrintWriter writ) {
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
            .childHandler(new ResponseListenerChannelInitializer(this::handleResponse));

        try {
            // Binds to the port
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

    /**
     * Channel response event listener. Will be sent to the response catcher, and will receive any response.
     *
     * @param ctx
     *            channel context
     * @param rsp
     *            response received
     */
    private final void handleResponse(final ChannelHandlerContext ctx, final String msg) {
        printRequest(msg);
        sendResponse(ctx, msg);
    }

    private final void printRequest(final String msg) {
        writer.printf("Received message: %s", msg);
        writer.println();
    }

    private final void sendResponse(final ChannelHandlerContext ctx, final String msg) {
        final ByteBuf                                               buf;
        final GenericFutureListener<? extends Future<? super Void>> listener;

        log.debug("Sending response", msg, response);

        writer.printf("Sending response: %s", response);
        writer.println();

        buf = Unpooled.wrappedBuffer(response.getBytes());

        // Reply listener
        listener = future -> {
            log.debug("Reply successful: {}", future.isSuccess());
            if (future.isSuccess()) {
                writer.println("Successful reply");
            } else {
                writer.println("Failed reply");
            }
        };

        ctx.writeAndFlush(buf)
            .addListener(listener);
    }

}
