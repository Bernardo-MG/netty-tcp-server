
package com.bernardomg.example.netty.tcp.server.codec;

import java.util.List;

import com.bernardomg.example.netty.tcp.server.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

public class NettyByteToMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final int     readableBytes;
        final String  msg;
        final Message message;

        readableBytes = in.readableBytes();
        if (readableBytes > 0) {
            msg = in.toString(CharsetUtil.UTF_8);
            in.readerIndex(in.readerIndex() + in.readableBytes());

            message = new Message();
            message.setMessage(msg);

            out.add(message);
        }
    }

}
