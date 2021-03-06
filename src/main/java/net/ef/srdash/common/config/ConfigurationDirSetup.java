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

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.status.StatusLogger;

import net.ef.srdash.common.ClassTools;
import net.ef.srdash.common.events.annotation.AutoExecutable;
import net.ef.srdash.common.events.annotation.Startup;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@AutoExecutable(name = "Configuration Dir Setup", priority = Integer.MAX_VALUE - 100)
public class ConfigurationDirSetup {

	private static final StatusLogger LOGGER = StatusLogger.getLogger();

	@Startup
	public void startup() {
		loadExternalLibs();
		loadProperties();
	}

	public static void loadExternalLibs() {
		File resourceDir = ConfigurationDir.getConfigFile("resources");
		if (resourceDir == null) {
			// no configuration dir exists
			return;
		}
		Optional<URLClassLoader> classLoaderOptional = Stream
				.of(ClassTools.getClassLoader(), Thread.currentThread().getContextClassLoader())
				.filter(URLClassLoader.class::isInstance).map(URLClassLoader.class::cast).findFirst();
		if (!classLoaderOptional.isPresent()) {
			error(classLoaderOptional.getClass() + " is unsupported for adding extending CLASSPATH at runtime.");
			return;
		}
		File libDir = ConfigurationDir.getConfigFile("lib");
		URLClassLoader urlClassLoader = classLoaderOptional.get();
		Set<URL> currentCPElements = Stream.of(urlClassLoader.getURLs()).collect(Collectors.toSet());
		Class<? extends ClassLoader> classLoaderClass = urlClassLoader.getClass();
		try {
			Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			ClassTools.trySetAccessible(addUrlMethod);
			getFileStream(resourceDir, libDir).map(File::toURI).map(u -> {
				try {
					return u.toURL();
				} catch (Exception e) {
					// should never happen for "file://" URIS
					return null;
				}
			}).filter(Objects::nonNull).filter(u -> !currentCPElements.contains(u)).forEach(u -> {
				info("Adding to CLASSPATH: " + u);
				try {
					addUrlMethod.invoke(urlClassLoader, u);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.error("Could not add {} to current classloader.", u, e);
				}
			});
		} catch (NoSuchMethodException | SecurityException e) {
			LogManager.getLogger().warn(classLoaderClass + " does not support adding additional JARs at runtime", e);
		}
	}

	public static void loadProperties() {
		ConfigurationLoader configurationLoader = new ConfigurationLoader();
		Map<String, String> properties = configurationLoader.load();
		Configuration.instance().initialize(properties, true);
	}

	private static Stream<File> getFileStream(File resourceDir, File libDir) {
		Stream<File> toClassPath = Stream.of(resourceDir);
		if (libDir.isDirectory()) {
			File[] listFiles = libDir
					.listFiles((FilenameFilter) (dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".jar"));
			if (listFiles.length != 0) {
				toClassPath = Stream.concat(toClassPath, Stream.of(listFiles));
			}
		}
		return toClassPath;
	}

	private static void error(String msg) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(msg);
		} else {
			System.err.println(
					Instant.now().toString() + " ERROR\t" + ConfigurationDirSetup.class.getSimpleName() + ": " + msg);
		}
	}

	private static void info(String msg) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(msg);
		} else {
			System.out.println(
					Instant.now().toString() + " INFO\t" + ConfigurationDirSetup.class.getSimpleName() + ": " + msg);
		}
	}
}
