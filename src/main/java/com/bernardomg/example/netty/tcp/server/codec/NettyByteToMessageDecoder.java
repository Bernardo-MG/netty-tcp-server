
package com.bernardomg.example.netty.tcp.server.codec;

import java.util.List;

import com.bernardomg.example.netty.tcp.server.model.ImmutableMessage;
import com.bernardomg.example.netty.tcp.server.model.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NettyByteToMessageDecoder extends ByteToMessageDecoder {

    public NettyByteToMessageDecoder() {
        super();
    }

    @Override
    protected final void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out)
            throws Exception {
        final int     readableBytes;
        final String  msg;
        final Message message;

        log.debug("Trying to decode response");

        readableBytes = in.readableBytes();
        if (readableBytes > 0) {
            log.debug("Received response");
            msg = in.toString(CharsetUtil.UTF_8);
            in.readerIndex(in.readerIndex() + in.readableBytes());

            message = new ImmutableMessage(msg);

            out.add(message);
        } else {
            log.debug("Received no response");
        }
    }

}
