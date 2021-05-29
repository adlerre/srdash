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
package net.ef.srdash.frontend;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;

import net.ef.srdash.common.ClassTools;
import net.ef.srdash.common.config.Configuration;
import net.ef.srdash.frontend.filter.CORSFilter;
import net.ef.srdash.frontend.filter.CacheFilter;
import net.ef.srdash.frontend.filter.IgnoreClientAbortInterceptor;
import net.ef.srdash.frontend.provider.EntityMessageBodyReader;
import net.ef.srdash.frontend.provider.EntityMessageBodyWriter;
import net.ef.srdash.frontend.provider.GenericExceptionMapper;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public class FrontendFeature implements Feature {

    private static final Logger LOGGER = LogManager.getLogger();

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.core.Feature#configure(javax.ws.rs.core.FeatureContext)
     */
    @Override
    public boolean configure(FeatureContext context) {
        // internal features
        context.register(CacheFilter.class);
        context.register(CORSFilter.class);
        context.register(IgnoreClientAbortInterceptor.class);

        context.register(EntityMessageBodyReader.class);
        context.register(EntityMessageBodyWriter.class);

        context.register(GenericExceptionMapper.class);

        // jersey features
        context.register(MoxyJsonFeature.class);
        context.register(MoxyXmlFeature.class);
        context.register(MultiPartFeature.class);

        Configuration.instance().getStrings("APP.Jersey.Features").forEach(cn -> {
            try {
                LOGGER.info("Register Jersey Feature: {}", cn);
                context.register(ClassTools.forName(cn));
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });

        return true;
    }
}
