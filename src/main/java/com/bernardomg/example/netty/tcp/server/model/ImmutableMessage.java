
package com.bernardomg.example.netty.tcp.server.model;

import lombok.Data;

@Data
public class ImmutableMessage implements Message {

    private String content;

    public ImmutableMessage(final String cnt) {
        super();

        content = cnt;
    }

}
