
package com.bernardomg.example.netty.tcp.server.channel;

/**
 * {@link WriteListener} is the lister message status interface.
 *
 * @author Sameer Narkhede See <a href="https://narkhedesam.com">https://narkhedesam.com</a>
 * @since Sept 2020
 *
 */
public interface WriteListener {

    void messageRespond(Boolean success);
}
