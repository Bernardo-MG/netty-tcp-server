
package com.bernardomg.example.netty.tcp.server.channel;

import java.io.PrintWriter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Taps into the channel and prints the message.
 *
 * @author bernardo.martinezg
 *
 */
@Slf4j
public final class ChannelMessageTap extends ChannelInboundHandlerAdapter {

    private final PrintWriter writer;

    public ChannelMessageTap(final PrintWriter writ) {
        super();

        writer = writ;
    }

    @Override
    public final void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        log.debug("Received message {}", msg);

        writer.printf("Received message: %s", msg);
        writer.println();

        super.channelRead(ctx, msg);
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        log.error(cause.getLocalizedMessage(), cause);
    }

}
