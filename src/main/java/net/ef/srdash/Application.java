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
package net.ef.srdash;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import net.ef.srdash.common.config.Configuration;
import net.ef.srdash.common.config.ConfigurationDir;
import net.ef.srdash.common.events.AutoExecutableHandler;
import net.ef.srdash.services.EmbeddedHttpServer;
import net.ef.srdash.services.TelemetryDataService;
import net.ef.srdash.telemetry.utils.PacketDeserializer;

public class Application {

    public static final String APP_NAME = "srdash";

    private static final Logger LOGGER = LogManager.getRootLogger();

    private static Application app;

    @Parameter(names = { "-h", "--help" }, description = "Print help (this message) and exit", help = true)
    private boolean help;

    @Parameter(names = { "--port", "-p" }, description = "Set port listen on")
    private Integer port = 8085;

    @Parameter(names = "--host", description = "Set host listen on")
    private String host;

    @Parameter(names = { "--configDir", "-cd" }, description = "Set configuration dir")
    private String configDir;

    @Parameter(names = { "--telemetryVersion", "-tv" }, description = "Set telemetry version")
    private Integer telemetryVersion;

    private Thread shutdownHook;

    private EmbeddedHttpServer embeddedHttpServer;

    private TelemetryDataService tdSvc;

    public static void main(String[] args) {
        app = new Application();
        JCommander jcmd = new JCommander(app, null, args);

        if (app.help) {
            jcmd.usage();
        } else {
            try {
                app.run();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static void exit() {
        try {
            app.stop();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private Application() {
        if (!(Application.class.getClassLoader() instanceof URLClassLoader)) {
            System.out.println("Current ClassLoader is not extendable at runtime. Using workaround.");
            Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0]));
        }
    }

    private void run() throws Exception {
        if (configDir != null) {
            ConfigurationDir.setConfigurationDirectory(configDir);
        }

        AutoExecutableHandler.setHaltOnError(false);
        shutdownHook = new Thread(() -> AutoExecutableHandler.shutdown());
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        AutoExecutableHandler.startup();

        tdSvc = new TelemetryDataService(Optional.ofNullable(telemetryVersion).orElse(PacketDeserializer.F1_AUTO));
        tdSvc.start();

        embeddedHttpServer = new EmbeddedHttpServer(host, port);
        embeddedHttpServer.start();

        Configuration.instance().set("APP.BaseURL",
                Optional.ofNullable(Configuration.instance().getString("APP.BaseURL", null))
                        .orElse(embeddedHttpServer.getURI().toString()));
    }

    private void stop() throws Exception {
        tdSvc.stop();
        embeddedHttpServer.stop();
        AutoExecutableHandler.shutdown();
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }
}
