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

import java.util.Map;
import java.util.Properties;

/**
 * Like {@link Properties} but with in-place replacement of properties that want
 * to append a value.
 * <p>
 * Properties for System.getProperties() have always precedence for Properties
 * defined here.
 *
 * <pre>
 * key=value1
 * key=%key%,value2
 * </pre>
 * <p>
 * will be resolved to
 *
 * <pre>
 * key=value1,value2
 * </pre>
 *
 * @author Ren\u00E9 Adler (eagle)
 */
public class AppProperties extends Properties {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6572230923593865161L;

    /* (non-Javadoc)
     * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public synchronized Object put(Object key, Object value) {
        return putString((String) key, (String) value);
    }

    /**
     * Put string.
     *
     * @param key   the key
     * @param value the value
     * @return the object
     */
    private Object putString(String key, String value) {
        String systemProperty = System.getProperties().getProperty(key);
        if (systemProperty != null && !systemProperty.equals(value)) {
            LogManager.getLogger(getClass()).error("Cannot overwrite system property: " + key + "=" + value);
            return systemProperty;
        }
        String oldValue = (String) super.get(key);
        String newValue = oldValue == null ? value : value.replaceAll('%' + key + '%', oldValue);
        if (!newValue.equals(value) && newValue.startsWith(",")) {
            // replacement took place, but starts with 'empty' value
            newValue = newValue.substring(1);
        }
        return super.put(key, newValue);
    }

    /* (non-Javadoc)
     * @see java.util.Hashtable#get(java.lang.Object)
     */
    @Override
    public synchronized Object get(Object key) {
        String systemProperty = System.getProperties().getProperty((String) key);
        return systemProperty != null ? systemProperty : super.get(key);
    }

    /**
     * Gets the as map.
     *
     * @return the as map
     */
    Map<String, String> getAsMap() {
        @SuppressWarnings("rawtypes")
        Map compileFix = this;
        @SuppressWarnings("unchecked")
        Map<String, String> returns = compileFix;
        return returns;
    }

    /**
     * Creates a new <code>AppProperties</code> instance with the values of the
     * given properties.
     *
     * @param properties the properties
     * @return the app properties
     */
    public static AppProperties copy(Properties properties) {
        AppProperties p = new AppProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            p.put(entry.getKey(), entry.getValue());
        }
        return p;
    }

}
