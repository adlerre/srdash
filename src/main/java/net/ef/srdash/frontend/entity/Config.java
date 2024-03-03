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
package net.ef.srdash.frontend.entity;

import jakarta.xml.bind.annotation.*;
import net.ef.srdash.common.config.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ren\u00E9 Adler (eagle)
 */
@XmlRootElement(name = "config")
@XmlType(name = "Config")
public class Config {

    private List<ConfigEntry> entries;

    public static Config from(Configuration cfg) {
        return new Config(cfg.getPropertiesMap().entrySet()
                .stream().map(e -> new ConfigEntry(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));
    }

    public static Config from(Configuration cfg, String startsWith) {
        return new Config(cfg.getPropertiesMap(startsWith).entrySet()
                .stream().map(e -> new ConfigEntry(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));
    }

    public Config() {
    }

    public Config(List<ConfigEntry> entries) {
        this();
        this.entries = entries;
    }

    @XmlElement(name = "entry")
    public List<ConfigEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ConfigEntry> entries) {
        this.entries = entries;
    }

    @XmlRootElement(name = "entry")
    @XmlType(name = "ConfigEntry")
    public static class ConfigEntry {

        private String key;

        private String value;

        public ConfigEntry() {
        }

        public ConfigEntry(String key, String value) {
            this();
            this.key = key;
            this.value = value;
        }

        @XmlAttribute(name = "key")
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @XmlValue
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
