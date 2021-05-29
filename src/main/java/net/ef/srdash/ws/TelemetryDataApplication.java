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
package net.ef.srdash.ws;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.grizzly.websockets.WebSocketListener;

import net.ef.srdash.common.events.Event;
import net.ef.srdash.common.events.EventManager;
import net.ef.srdash.common.events.Listener;
import net.ef.srdash.common.events.annotation.AutoExecutable;
import net.ef.srdash.common.events.annotation.Shutdown;
import net.ef.srdash.common.events.annotation.Startup;
import net.ef.srdash.telemetry.data.Packet;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@AutoExecutable(name = "Telemetry Data Websocket")
public class TelemetryDataApplication extends WebSocketApplication implements Listener {

    private static TelemetryDataApplication app;

    @Startup
    protected static void register() {
        app = new TelemetryDataApplication();
        WebSocketEngine.getEngine().register("", "/ws/telemetry", app);
    }

    @Shutdown
    protected static void unregister() {
        WebSocketEngine.getEngine().unregister(app);
    }

    public TelemetryDataApplication() {
        EventManager.instance().addListener(this);
    }

    /* (non-Javadoc)
     * @see org.glassfish.grizzly.websockets.WebSocketApplication#createSocket(org.glassfish.grizzly.websockets.ProtocolHandler, org.glassfish.grizzly.http.HttpRequestPacket, org.glassfish.grizzly.websockets.WebSocketListener[])
     */
    @Override
    public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket requestPacket,
            WebSocketListener... listeners) {
        return new TelemetryDataWebSocket(handler, listeners);
    }

    @Override
    public <T> void handleEvent(Event<T> event) throws Exception {
        if (event.getType().equals(Packet.class.getSimpleName())) {
            Packet packet = (Packet) event.getObject();
            String json = packet.toJSON();
            this.getWebSockets().parallelStream().filter(WebSocket::isConnected).forEach(ws -> ws.send(json));
        }
    }

}
