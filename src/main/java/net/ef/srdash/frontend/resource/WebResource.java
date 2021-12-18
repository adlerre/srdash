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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.security.PermitAll;
import javax.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ef.srdash.common.ClassTools;
import net.ef.srdash.frontend.annotation.CacheControl;
import net.ef.srdash.frontend.entity.ExceptionWrapper;
import net.ef.srdash.frontend.entity.ResourceWrapper;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@Path("/")
@Singleton
public class WebResource {

    private static final Logger LOGGER = LogManager.getLogger(WebResource.class);

    private static final ClassLoader CLASS_LOADER = ClassTools.getClassLoader();

    private static final String RESOURCE_DIR = "META-INF/resources";

    private static final String INDEX_FILE = "index.html";

    @PermitAll
    @GET
    @CacheControl(maxAge = @CacheControl.Age(time = 30, unit = TimeUnit.DAYS), proxyRevalidate = true)
    @Produces("*/*")
    public Response getWebResource() {
        return getWebResource(INDEX_FILE);
    }

    @PermitAll
    @GET
    @CacheControl(maxAge = @CacheControl.Age(time = 30, unit = TimeUnit.DAYS), proxyRevalidate = true)
    @Path("{fileName:.+}")
    @Produces("*/*")
    public Response getWebResource(@PathParam("fileName") String fileName) {
        try {
            String fn = checkResourcePath(fileName, false);

            if (fn == null && fileName.lastIndexOf(".") == -1) {
                fn = INDEX_FILE;
                LOGGER.info("fallback to \"{}\"", fn);
            }

            final Optional<ResourceWrapper> res = getResource(fn);
            if (res.isPresent()) {
                final ResourceWrapper r = res.get();
                return Response.ok().tag(r.getETag()).type(r.getMimeType()).entity(r.getContent()).build();
            } else {
                LOGGER.error("resource \"{}\" not found.", fileName);
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionWrapper(e)).build();
        }
    }

    private String checkResourcePath(String fileName, boolean allowNull) {
        final String fn = RESOURCE_DIR + File.separator + fileName;

        if (CLASS_LOADER.getResource(fn) != null) {
            return fileName;
        } else if (allowNull) {
            return null;
        }

        return checkResourcePath(fileName + File.separator + INDEX_FILE, true);
    }

    private Optional<ResourceWrapper> getResource(String fileName) {
        final String fn = RESOURCE_DIR + File.separator + fileName;

        try (final InputStream is = CLASS_LOADER.getResourceAsStream(fn)) {
            final ResourceWrapper res = new ResourceWrapper(fn, is);
            LOGGER.info("loaded resource \"{}\" with mime type \"{}\"", fileName, res.getMimeType());
            return Optional.of(res);
        } catch (NullPointerException e) {
            return Optional.empty();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}