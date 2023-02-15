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

package com.bernardomg.example.netty.tcp.cli.command;

import java.io.PrintWriter;

import com.bernardomg.example.netty.tcp.cli.version.ManifestVersionProvider;
import com.bernardomg.example.netty.tcp.server.NettyTcpServer;
import com.bernardomg.example.netty.tcp.server.Server;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

/**
 * Dice gatherer command. Receives an expression, gets all the dice sets on it and prints the result on screen.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Command(name = "start", description = "Starts a TCP server", mixinStandardHelpOptions = true,
        versionProvider = ManifestVersionProvider.class)
public final class StartServerCommand implements Runnable {

    @Parameters(index = "0", description = "Server port", paramLabel = "PORT")
    private Integer     port;

    @Parameters(index = "1", description = "Server response", paramLabel = "RESP", defaultValue = "Acknowledged")
    private String      response;

    /**
     * Command specification. Used to get the line output.
     */
    @Spec
    private CommandSpec spec;

    /**
     * Default constructor.
     */
    public StartServerCommand() {
        super();
    }

    @Override
    public final void run() {
        final PrintWriter writer;
        final Server      server;

        writer = spec.commandLine()
            .getOut();

        // Prints the final result
        writer.println();
        writer.println("------------");
        writer.printf("Starting server and listening to port %d", port);
        writer.println();
        writer.println("------------");

        server = new NettyTcpServer(port, response, writer);

        server.start();
    }

}
