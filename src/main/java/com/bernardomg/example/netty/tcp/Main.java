
package com.bernardomg.example.netty.tcp;

import com.bernardomg.example.netty.tcp.cli.TcpServerMenu;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        final Integer exitCode;

        log.error("Error");

        exitCode = new CommandLine(new TcpServerMenu()).execute(args);

        // log.debug("Exited with code {}", exitCode);
        //
        // System.exit(exitCode);
    }

    public Main() {
        super();
    }

}
