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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ef.srdash.common.config.Configuration;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public class EventManager {

	private static final String CONFIG_PREFIX = "APP.EventListener.";

	private static final Logger LOGGER = LogManager.getLogger();

	private static EventManager INSTANCE = null;

	private final Map<String, Listener> listeners = new ConcurrentHashMap<>();

	public static EventManager instance() {
		if (INSTANCE == null) {
			synchronized (EventManager.class) {
				if (INSTANCE == null) {
					INSTANCE = new EventManager();
				}
			}
		}

		return INSTANCE;
	}

	private EventManager() {
	}

	public void addListener(final Listener listener) {
		addListener(listener.getClass(), listener);
	}

	public void addListener(final Class<? extends Listener> clazz, final Listener listener) {
		if (Configuration.instance().getBoolean(CONFIG_PREFIX + clazz.getName() + ".activated", true)) {
			LOGGER.info("Register Listener: {}", clazz.getName());
			listeners.put(clazz.getName(), listener);
		}
	}

	public void removeListner(final Listener listener) {
		removeListner(listener.getClass(), listener);
	}

	public void removeListner(final Class<? extends Listener> clazz, final Listener listener) {
		listeners.remove(listener.getClass().getName());
	}

	public void fireEvent(final Event<?> event) {
		listeners.values().forEach(d -> {
			try {
				d.handleEvent(event);
			} catch (Exception ex) {
				LogManager.getLogger(d.getClass()).error(ex.getMessage(), ex);
				throw new RuntimeException(ex);
			}
		});
	}

	public void fireEvent(final Class<? extends Listener> delegate, final Event<?> event) {
		listeners.entrySet().stream().filter(e -> e.getKey().equals(delegate.getName())).findFirst().ifPresent(e -> {
			try {
				e.getValue().handleEvent(event);
			} catch (Exception ex) {
				LogManager.getLogger(e.getValue().getClass()).error(ex.getMessage(), ex);
			}
		});
	}
}
