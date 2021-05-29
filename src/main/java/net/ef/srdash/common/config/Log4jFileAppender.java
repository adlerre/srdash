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
package net.ef.srdash.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import net.ef.srdash.common.events.annotation.AutoExecutable;
import net.ef.srdash.common.events.annotation.Startup;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@AutoExecutable(name = "Log4j LogFile Appender", priority = 2000)
public class Log4jFileAppender {

    private static final Configuration CONFIG = Configuration.instance();

    private static final String CONFIG_PREFIX = "APP.Log4j.";

    @Startup
    public void startup() {
        addFileAppender();
    }

    private void addFileAppender() {
        final String filePath = CONFIG.getString(CONFIG_PREFIX + "File", null);

        if (filePath != null) {
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();

            PatternLayout layout = PatternLayout.newBuilder().withConfiguration(config)
                    .withPattern(CONFIG.getString(CONFIG_PREFIX + "Pattern",
                            "%d{ISO8601} %X{remoteIP} %X{user} %-5p %c{1}: %m%n%ex"))
                    .build();

            Appender appender = FileAppender.newBuilder().withFileName(filePath)
                    .withAppend(true)
                    .setIgnoreExceptions(false).setName("File").setLayout(layout).build();

            appender.start();

            config.getRootLogger().addAppender(appender, null, null);
        }
    }

}
