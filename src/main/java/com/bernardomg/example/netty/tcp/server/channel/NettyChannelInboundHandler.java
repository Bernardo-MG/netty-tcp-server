
package com.bernardomg.example.netty.tcp.server.channel;

import java.io.PrintWriter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NettyChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private final String      response;

    private final PrintWriter writer;

    public NettyChannelInboundHandler(final String resp, final PrintWriter writ) {
        super();

        response = resp;
        writer = writ;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final ByteBuf                                               buf;
        final GenericFutureListener<? extends Future<? super Void>> listener;

        log.debug("Received message {} and sending response", msg, response);

        writer.printf("Received message: %s", msg);
        writer.println();

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

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        log.error(cause.getLocalizedMessage(), cause);
    }

}
