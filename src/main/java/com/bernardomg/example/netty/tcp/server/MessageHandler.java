
package com.bernardomg.example.netty.tcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * {@link WriteListener} is the lister message status interface.
     *
     * @author Sameer Narkhede See <a href="https://narkhedesam.com">https://narkhedesam.com</a>
     * @since Sept 2020
     *
     */
    public interface WriteListener {

        void messageRespond(boolean success);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }

    /**
     * Actual Message handling and reply to server.
     *
     * @param ctx
     *            {@link ChannelHandlerContext}
     * @param msg
     *            {@link Message}
     */
    private void handleMessage(final ChannelHandlerContext ctx, final Message msg) {

        System.out.println("Message Received : " + msg.getMessage());

        final ByteBuf       buf      = Unpooled.wrappedBuffer("Hey Sameer Here!!!!".getBytes());

        // Send reply
        final WriteListener listener = success -> System.out.println(success ? "reply success" : "reply fail");

        ctx.writeAndFlush(buf)
            .addListener(future -> {
                if (listener != null) {
                    listener.messageRespond(future.isSuccess());
                }
            });
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Message msg) throws Exception {

        handleMessage(ctx, msg);

    }

}
