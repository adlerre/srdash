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
package net.ef.srdash.common.utils.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ef.srdash.common.utils.ObjectTools.FieldMappingAdapter;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
@Repeatable(FieldMapping.FieldMappings.class)
/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public @interface FieldMapping {

	/**
	 * The <code>class</code> from witch mapping is allowed.
	 * 
	 * @return the class
	 */
	Class<?> from();

	/**
	 * The field<code>name</code> from witch should mapped.
	 * 
	 * @return the field name
	 */
	String name();

	@SuppressWarnings("rawtypes")
	Class<? extends FieldMappingAdapter> adapter() default DEFAULT.class;

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(FIELD)
	public @interface FieldMappings {
		FieldMapping[] value();
	}

	static final class DEFAULT extends FieldMappingAdapter<Object, Object> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.wpp.utils.ObjectTools.FieldMappingAdapter#
		 * mapTo(java.lang.Object)
		 */
		@Override
		public Object mapTo(Object v) {
			return v;
		}

	}
}
