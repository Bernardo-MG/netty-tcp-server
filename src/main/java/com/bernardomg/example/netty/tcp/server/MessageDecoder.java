
package com.bernardomg.example.netty.tcp.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {

        final int readableBytes = in.readableBytes();
        if (readableBytes <= 0) {
            return;
        }

        final String msg = in.toString(CharsetUtil.UTF_8);
        in.readerIndex(in.readerIndex() + in.readableBytes());

        final Message message = new Message();
        message.setMessage(msg);

        out.add(message);

    }

}
