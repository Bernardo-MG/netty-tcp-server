
package com.bernardomg.example.netty.tcp.server.channel;

import com.bernardomg.example.netty.tcp.server.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public final class NettySimpleChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

    public NettySimpleChannelInboundHandler() {
        super();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Message msg) throws Exception {
        final ByteBuf       buf;
        final WriteListener listener;

        // TODO: Send to console
        System.out.println("Message Received : " + msg.getMessage());

        // TODO: The response should be configurable
        buf = Unpooled.wrappedBuffer("Hey Sameer Here!!!!".getBytes());

        // Send reply
        listener = success -> {
            if (success) {
                // TODO: Send to console
                System.out.println("reply success");
            } else {
                // TODO: Send to console
                System.out.println("reply fail");
            }
        };

        ctx.writeAndFlush(buf)
            .addListener(future -> {
                listener.messageRespond(future.isSuccess());
            });
    }

}
