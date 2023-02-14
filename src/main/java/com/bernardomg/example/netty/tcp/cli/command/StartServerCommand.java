/**
 * Copyright 2020-2022 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bernardomg.example.netty.tcp.cli.command;

import java.io.PrintWriter;

import com.bernardomg.example.netty.tcp.cli.version.ManifestVersionProvider;
import com.bernardomg.example.netty.tcp.server.MessagePipelineFactory;
import com.bernardomg.example.netty.tcp.server.NettyServer;
import com.bernardomg.example.netty.tcp.server.Server;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public final class StartServerCommand implements Runnable {

    @Parameters(index = "0", description = "Server port", paramLabel = "PORT")
    private Integer     port;

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
        writer.printf("Starting server at port %d", port);
        writer.println();
        writer.println("------------");

        server = new NettyServer(MessagePipelineFactory.class);

        try {
            log.debug("Starting server");
            server.startup(port);
            log.debug("Stopped server");
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
        }
    }

}
