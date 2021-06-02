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
package net.ef.srdash.frontend.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ef.srdash.common.config.Configuration;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@Path("/system")
@Singleton
public class SystemResource {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Configuration CONFIG = Configuration.instance();

    @Path("reboot")
    @PermitAll
    @PUT
    @Produces("*/*")
    public ProcessResult doReboot() throws IOException, InterruptedException {
        String rebootCommand = CONFIG.getString("System.cmd.reboot", null);

        if (rebootCommand == null) {
            String operatingSystem = System.getProperty("os.name");

            if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
                rebootCommand = "shutdown -r now";
            } else if ("Windows".equals(operatingSystem)) {
                rebootCommand = "shutdown.exe -r -t 0";
            } else {
                throw new RuntimeException("Unsupported operating system.");
            }
        }

        ProcessResult res = runCommand(rebootCommand);
        LOGGER.info(res);

        return res;
    }

    @Path("shutdown")
    @PermitAll
    @PUT
    @Produces("*/*")
    public ProcessResult doShutdown() throws IOException, InterruptedException {
        String shutdownCommand = CONFIG.getString("System.cmd.shutdown", null);

        if (shutdownCommand == null) {
            String operatingSystem = System.getProperty("os.name");

            if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
                shutdownCommand = "shutdown -h now";
            } else if ("Windows".equals(operatingSystem)) {
                shutdownCommand = "shutdown.exe -s -t 0";
            } else {
                throw new RuntimeException("Unsupported operating system.");
            }
        }

        ProcessResult res = runCommand(shutdownCommand);
        LOGGER.info(res);

        return res;
    }

    private ProcessResult runCommand(String cmd) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(cmd);

        StringBuffer sbOut = new StringBuffer();
        StringBuffer sbErr = new StringBuffer();

        Thread ioThreadOutput = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sbOut.append(line);
                    }
                    reader.close();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        };

        Thread ioThreadError = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sbErr.append(line);
                    }
                    reader.close();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        };

        ioThreadOutput.start();
        ioThreadError.start();

        p.waitFor();

        return new ProcessResult(sbOut.toString(), sbErr.toString());
    }

    @XmlRootElement(name = "process-result")
    public static class ProcessResult {

        private String output;

        private String error;

        private ProcessResult() {
        }

        /**
         * @param output
         * @param error
         */
        public ProcessResult(String output, String error) {
            this();
            this.output = output;
            this.error = error;
        }

        /**
         * @return the output
         */
        @XmlElement
        public String getOutput() {
            return output;
        }

        /**
         * @param output the output to set
         */
        public void setOutput(String output) {
            this.output = output;
        }

        /**
         * @return the error
         */
        @XmlElement
        public String getError() {
            return error;
        }

        /**
         * @param error the error to set
         */
        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return "ProcessResult [output=" + output + ", error=" + error + "]";
        }

    }

}
