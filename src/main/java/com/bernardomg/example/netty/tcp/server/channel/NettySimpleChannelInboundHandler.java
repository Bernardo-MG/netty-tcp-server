
package com.bernardomg.example.netty.tcp.server.channel;

import java.io.PrintWriter;

import com.bernardomg.example.netty.tcp.server.model.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NettySimpleChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

    private final String      response;

    private final PrintWriter writer;

    public NettySimpleChannelInboundHandler(final String resp, final PrintWriter writ) {
        super();

        response = resp;
        writer = writ;
    }

    @Override
    protected final void channelRead0(final ChannelHandlerContext ctx, final Message msg) throws Exception {
        final ByteBuf       buf;
        final WriteListener listener;

        writer.printf("Received message: %s", msg.getContent());
        writer.println();

        writer.printf("Sending response: %s", response);
        writer.println();

        buf = Unpooled.wrappedBuffer(response.getBytes());

        // Reply listener
        listener = success -> {
            if (success) {
                writer.println("Successful reply");
            } else {
                writer.println("Failed reply");
            }
        };

        ctx.writeAndFlush(buf)
            .addListener(future -> {
                listener.messageRespond(future.isSuccess());
            });
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        log.error(cause.getLocalizedMessage(), cause);
    }

}
