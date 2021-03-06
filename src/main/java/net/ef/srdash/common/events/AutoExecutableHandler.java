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
package net.ef.srdash.common.events;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import net.ef.srdash.Application;
import net.ef.srdash.common.ClassTools;
import net.ef.srdash.common.events.annotation.AutoExecutable;
import net.ef.srdash.common.events.annotation.Shutdown;
import net.ef.srdash.common.events.annotation.Startup;

/**
 * The Class AutoExecutableHandler.
 *
 * @author Ren\u00E9 Adler (eagle)
 */
public class AutoExecutableHandler {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager.getLogger();

    /** The halt on error. */
    private static boolean haltOnError = true;

    /** The executables. */
    private static Set<Class<?>> executables;

    static {
        final Reflections reflections = new Reflections(Application.class.getPackage().getName(),
                new TypeAnnotationsScanner());
        executables = reflections.getTypesAnnotatedWith(AutoExecutable.class, true);
    }

    /**
     * Register class as {@link AutoExecutable}.
     *
     * @param executable
     *            the class to execute
     */
    public static void register(Class<?> executable) {
        if (executable.isAnnotationPresent(AutoExecutable.class)) {
            executables.add(executable);
        } else {
            LOGGER.warn("Class \"" + executable.getName() + "\" should have the \"AutoExecutable\" annotation.");
        }
    }

    /**
     * Checks if is halt on error.
     *
     * @return the haltOnError
     */
    public static boolean isHaltOnError() {
        return haltOnError;
    }

    /**
     * Sets the halt on error.
     *
     * @param haltOnError
     *            the haltOnError to set
     */
    public static void setHaltOnError(boolean haltOnError) {
        AutoExecutableHandler.haltOnError = haltOnError;
    }

    /**
     * Starts registered startup hooks.
     */
    public static void startup() {
        executables.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getAnnotation(AutoExecutable.class).priority(),
                        o1.getAnnotation(AutoExecutable.class).priority()))
                .forEachOrdered(autoExecutable -> runExecutables(autoExecutable, Startup.class));
    }

    /**
     * Starts registered shutdown hooks.
     */
    public static void shutdown() {
        executables.stream()
                .sorted((o1, o2) -> Integer.compare(o1.getAnnotation(AutoExecutable.class).priority(),
                        o2.getAnnotation(AutoExecutable.class).priority()))
                .forEachOrdered(autoExecutable -> runExecutables(autoExecutable, Shutdown.class));
    }

    /**
     * Run executables.
     *
     * @param autoExecutable
     *            the auto executable
     * @param type
     *            the type
     */
    private static void runExecutables(Class<?> autoExecutable, Class<? extends Annotation> type) {
        log("Run " + autoExecutable.getAnnotation(AutoExecutable.class).name() + " with priority of "
                + autoExecutable.getAnnotation(AutoExecutable.class).priority());
        try {
            sort(type, Arrays.stream(autoExecutable.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(type)))
                    .forEachOrdered(m -> {
                        try {
                            log("...invoke " + m.getName() + "() for " + type.getSimpleName());
                            m.setAccessible(true);
                            if (Modifier.isStatic(m.getModifiers())) {
                                m.invoke(null);
                            } else {
                                m.invoke(ClassTools.newInstance(autoExecutable, false));
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e.getCause());
                        }
                    });
        } catch (RuntimeException e) {
            if (haltOnError) {
                throw e;
            }
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * Sort.
     *
     * @param type
     *            the type
     * @param methods
     *            the methods
     * @return the stream
     */
    private static Stream<Method> sort(Class<? extends Annotation> type, Stream<Method> methods) {
        if (type.equals(Startup.class)) {
            // reverse ordering: highest priority first
            return methods.sorted((o1, o2) -> Integer.compare(o2.getAnnotation(Startup.class).priority(),
                    o1.getAnnotation(Startup.class).priority()));
        } else if (type.equals(Shutdown.class)) {
            return methods.sorted((o1, o2) -> Integer.compare(o1.getAnnotation(Shutdown.class).priority(),
                    o2.getAnnotation(Shutdown.class).priority()));
        }

        return methods;
    }

    /**
     * Log.
     *
     * @param msg
     *            the msg
     */
    private static void log(String msg) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(msg);
        } else {
            System.out.println(
                    Instant.now().toString() + " INFO\t" + AutoExecutableHandler.class.getSimpleName() + ": " + msg);
        }
    }
}
