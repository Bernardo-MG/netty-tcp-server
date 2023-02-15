
package com.bernardomg.example.netty.tcp;

import com.bernardomg.example.netty.tcp.cli.TcpServerMenu;

import picocli.CommandLine;

public class Main {

    public static void main(final String[] args) {
        new CommandLine(new TcpServerMenu()).execute(args);
    }

    public Main() {
        super();
    }

}
