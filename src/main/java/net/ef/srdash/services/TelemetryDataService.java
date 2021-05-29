/*
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 3
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package net.ef.srdash.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ef.srdash.common.events.Event;
import net.ef.srdash.common.events.EventManager;
import net.ef.srdash.telemetry.data.Packet;
import net.ef.srdash.telemetry.utils.PacketDeserializer;

/**
 * The base class for the Telemetry app. Starts up a non-blocking I/O
 * UDP server to read packets from the F1/Dirt video game and then hands those
 * packets off to a parallel thread for processing based on the lambda function
 * defined. Leverages a fluent API for initialization.
 * 
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public class TelemetryDataService {

    private final int version;

    private ExecutorService executor;

    public TelemetryDataService() {
        this(PacketDeserializer.F1_2018);
    }

    public TelemetryDataService(int version) {
        this.version = version;
    }

    public void start() throws IOException {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new UDPServer((p) -> {
            if (p != null) {
                EventManager.instance().fireEvent(new Event<Packet>(Packet.class.getSimpleName(), p, this.getClass()));
            }
        }, version));
    }

    public void stop() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    public static class UDPServer implements Runnable {

        public static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";

        public static final int DEFAULT_PORT = 20777;

        private static final Logger LOGGER = LogManager.getLogger();

        private final String bindAddress;

        private final int port;

        private final Consumer<Packet> packetConsumer;

        private int version;

        public UDPServer(String bindAddress, int port, Consumer<Packet> packetConsumer, int version) {
            this.bindAddress = bindAddress;
            this.port = port;
            this.packetConsumer = packetConsumer;
            this.version = version;
        }

        public UDPServer(Consumer<Packet> packetConsumer, int version) {
            this(DEFAULT_BIND_ADDRESS, DEFAULT_PORT, packetConsumer, version);

        }

        @Override
        public void run() {
            LOGGER.info("Telemetry UDP Server for version {}", version);

            ExecutorService executor = Executors.newSingleThreadExecutor();

            try (DatagramChannel channel = DatagramChannel.open()) {
                channel.socket().bind(new InetSocketAddress(bindAddress, port));
                LOGGER.info("Listening on " + bindAddress + ":" + port + "...");
                ByteBuffer buf = ByteBuffer.allocate(PacketDeserializer.maxPacketSize(version));
                buf.order(ByteOrder.LITTLE_ENDIAN);
                while (true) {
                    channel.receive(buf);
                    final Packet packet = PacketDeserializer.read(buf.array(), version);
                    executor.submit(() -> {
                        packetConsumer.accept(packet);
                    });
                    buf.clear();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                LOGGER.info("...shutdown.");
                executor.shutdown();
            }
        }

    }

}
