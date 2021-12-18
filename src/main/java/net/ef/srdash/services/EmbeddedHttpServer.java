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
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Locale;

import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriBuilderException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;

import net.ef.srdash.common.config.Configuration;
import net.ef.srdash.frontend.FrontendFeature;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public class EmbeddedHttpServer {
    private static final Logger LOGGER = LogManager.getLogger(EmbeddedHttpServer.class);

    private HttpServer server;

    private String host;

    private Integer port = 8085;

    public EmbeddedHttpServer(final String host, final Integer port) {
        this.host = host;
        if (port != null && port.intValue() > 0)
            this.port = port;
    }

    public void start() throws Exception {
        LOGGER.info("Starting Embedded HTTP Server...");
        if (server == null) {
            server = createHttpServer();

            LOGGER.info(String.format(Locale.ROOT,
                    "Jersey Application Server started with WADL available at %sapplication.wadl", getURI()));

            server.getListeners().forEach(listener -> listener.registerAddOn(new WebSocketAddOn()));
        }
        server.start();
    }

    public void stop() throws Exception {
        LOGGER.info("Stopping Embedded HTTP Server...");
        server.shutdown();
    }

    public URI getURI() throws IllegalArgumentException, UriBuilderException, URISyntaxException {
        return getURI(this.port);
    }

    public URI getURI(Integer port) throws IllegalArgumentException, UriBuilderException, URISyntaxException {
        return UriBuilder.fromUri(new URI("http://" + getHostName() + "/")).port(port.intValue()).build();
    }

    private HttpServer createHttpServer()
            throws IOException, IllegalArgumentException, UriBuilderException, URISyntaxException {
        ResourceConfig resourceConfig = new ResourceConfig()
                .packages(true, Configuration.instance().getStrings("APP.Jersey.Resources").toArray(new String[0]))
                .register(FrontendFeature.class);

        EncodingFilter.enableFor(resourceConfig, GZipEncoder.class);
        return GrizzlyHttpServerFactory.createHttpServer(getURI(), resourceConfig, false);
    }

    private String getHostName() {
        if (this.host != null && !this.host.isEmpty())
            return this.host;

        String hostName = "0.0.0.0";
        try {
            hostName = InetAddress.getByName(hostName).getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return hostName;
    }
}
