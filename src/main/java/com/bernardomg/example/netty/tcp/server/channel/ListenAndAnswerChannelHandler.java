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

package com.bernardomg.example.netty.tcp.server.channel;

import java.nio.charset.Charset;
import java.util.Objects;

import com.bernardomg.example.netty.tcp.server.TransactionListener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Message listener and answerer channel handler. Will send any message to the contained listener, and will send back a
 * predefined response.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class ListenAndAnswerChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * Transaction listener. Reacts to events during the request.
     */
    private final TransactionListener listener;

    /**
     * Response to send after a request.
     */
    private final String              messageForClient;

    public ListenAndAnswerChannelHandler(final String msg, final TransactionListener lst) {
        super();

        messageForClient = Objects.requireNonNull(msg);
        listener = Objects.requireNonNull(lst);
    }

    @Override
    public final void channelRead(final ChannelHandlerContext ctx, final Object message) throws Exception {
        final ByteBuf buf;

        log.debug("Received message {}", message);

        listener.onReceive(message.toString());

        buf = Unpooled.wrappedBuffer(messageForClient.getBytes(Charset.defaultCharset()));

        ctx.writeAndFlush(buf)
            .addListener(future -> {
                log.debug("Sending response: {}", messageForClient);

                listener.onSend(messageForClient);
            });

        ctx.fireChannelRead(message);
    }

}
