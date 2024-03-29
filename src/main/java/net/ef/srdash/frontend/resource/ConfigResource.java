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

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import net.ef.srdash.common.config.Configuration;
import net.ef.srdash.frontend.entity.Config;

import javax.inject.Singleton;

/**
 * @author Ren\u00E9 Adler (eagle)
 */
@Path("/config")
@Singleton
public class ConfigResource {

    @PermitAll
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Config getConfiguration() {
        return Config.from(Configuration.instance());
    }

    @PermitAll
    @Path("/{startsWith:.*}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Config getConfiguration(@PathParam("startsWith") String startsWith) {
        return Config.from(Configuration.instance(), startsWith);
    }

}
