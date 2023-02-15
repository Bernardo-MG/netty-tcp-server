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
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty based TCP server.
 *
 * @author bernardo.martinezg
 *
 */
@Slf4j
public final class NettyTcpServer implements Server {

    private EventLoopGroup    bossLoopGroup;

    private ChannelGroup      channelGroup;

    /**
     * Port which the server will listen to.
     */
    private final Integer     port;

    /**
     * Response to send after a request.
     */
    private final String      response;

    private EventLoopGroup    workerLoopGroup;

    /**
     * CLI writer, to print console messages.
     */
    private final PrintWriter writer;

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

        writer.println();
        writer.println("------------");
        writer.printf("Starting server and listening to port %d", port);
        writer.println();
        writer.println("------------");

        // Initializes groups
        bossLoopGroup = new NioEventLoopGroup();
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        workerLoopGroup = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        bootstrap
            // Registers groups
            .group(bossLoopGroup, workerLoopGroup)
            // Defines channel
            .channel(NioServerSocketChannel.class)
            // Configuration
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.AUTO_CLOSE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            // Child handler
            .childHandler(new ResponseListenerChannelInitializer(this::handleRequest));

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

        if (channelFuture.isSuccess()) {
            log.debug("Bound correctly to port {}", port);
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
     * Channel request event listener. Will receive any request sent by the client.
     *
     * @param ctx
     *            channel context
     * @param rsp
     *            response received
     */
    private final void handleRequest(final ChannelHandlerContext ctx, final String msg) {
        final ByteBuf buf;

        log.debug("Sending response", msg, response);

        buf = Unpooled.wrappedBuffer(response.getBytes());

        ctx.writeAndFlush(buf)
            .addListener(future -> printTransactionContext(msg, future.isSuccess()));
    }

    /**
     * Prints to console the transaction context.
     *
     * @param request
     *            request sent by the client
     * @param successful
     *            if the response was successful
     */
    private final void printTransactionContext(final String request, final Boolean successful) {
        log.debug("Reply successful: {}", successful);

        // Write context to console
        writer.println();
        writer.printf("Received message: %s", request);
        writer.println();
        writer.printf("Sending response: %s", response);
        writer.println();

        if (successful) {
            writer.println("Successful response");
        } else {
            writer.println("Failed response");
        }
    }

}
