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
package net.ef.srdash.common.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import net.ef.srdash.common.ClassTools;
import net.ef.srdash.common.utils.annotation.FieldMapping;
import net.ef.srdash.common.utils.annotation.SkipMerge;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public class ObjectTools {

    private static final Method METHOD_ADD_LIST = ObjectTools.searchMethod(List.class, "add", Object.class);
    private static final Method METHOD_REMOVE_LIST = ObjectTools.searchMethod(List.class, "remove", Object.class);

    public static <T> T merge(T dst, T src) {
        return merge(dst, src, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> T merge(T dst, T src, boolean allowNull) {
        Class<?> cls = dst.getClass();
        Arrays.stream(cls.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers())
                && !Modifier.isFinal(f.getModifiers()) && !f.isAnnotationPresent(SkipMerge.class)).forEach(f -> {
                    try {
                        ClassTools.trySetAccessible(f);

                        Optional<Class<?>> valCls = Optional
                                .ofNullable(Optional.ofNullable(f.get(dst)).orElse(f.get(src))).map(Object::getClass);
                        if (valCls.isPresent() && List.class.isAssignableFrom(valCls.get())
                                && !Objects.isNull(f.get(dst))) {
                            mergeList((List<Object>) getValue(dst, f), (List<Object>) getValue(src, f), allowNull);
                        } else {
                            Optional<Method> setter = fieldSetter(cls, f);

                            Object value = !allowNull && isFieldValueRequired(cls, f)
                                    ? Optional
                                            .ofNullable(Optional.ofNullable(getValue(src, f)).orElse(getValue(dst, f)))
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Value for " + f + " is required."))
                                    : allowNull ? getValue(src, f)
                                            : Optional.ofNullable(getValue(src, f)).orElse(getValue(dst, f));

                            if (setter.isPresent()) {
                                Method m = setter.get();

                                ClassTools.trySetAccessible(m);
                                m.invoke(dst, value);
                            } else {
                                f.set(dst, value);
                            }
                        }
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
        return dst;
    }

    public static <T> void mergeList(List<T> dst, List<T> src) {
        mergeList(dst, src, true);
    }

    public static <T> void mergeList(List<T> dst, List<T> src, boolean allowNull) {
        mergeList(dst, src, dst, allowNull, Optional.empty(), Optional.empty());
    }

    public static <T> void mergeList(List<T> dst, List<T> src, Object dstCont, Optional<Method> addMethod,
            Optional<Method> removeMethod) {
        mergeList(dst, src, dstCont, true, addMethod, removeMethod);
    }

    public static <T> void mergeList(List<T> dst, List<T> src, Object dstCont, boolean allowNull,
            Optional<Method> addMethod, Optional<Method> removeMethod) {
        if (src == null) {
            return;
        }
        dst.stream().filter(src::contains).forEach(a -> {
            src.stream().filter(a::equals).findFirst().ifPresent(b -> merge(a, b, allowNull));
        });
        List<T> dstRemove = dst.stream().filter(a -> !src.contains(a)).collect(Collectors.toList());
        dstRemove.stream().forEach(a -> {
            try {
                removeMethod.orElseGet(() -> METHOD_REMOVE_LIST).invoke(dstCont, a);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | SecurityException e) {
                throw new RuntimeException(e);
            }
        });
        src.stream().filter(b -> b != null && !dst.contains(b)).iterator().forEachRemaining(b -> {
            try {
                addMethod.orElseGet(() -> METHOD_ADD_LIST).invoke(dstCont, b);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | SecurityException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static boolean isFieldValueRequired(Class<?> cls, Field f) {
        Optional<Method> m = fieldGetter(cls, f);

        if (f.isAnnotationPresent(XmlElement.class)
                || m.map(om -> om.isAnnotationPresent(XmlElement.class)).orElse(false)) {
            return Optional.ofNullable(f.getAnnotation(XmlElement.class)).map(XmlElement::required).orElse(false)
                    || m.map(om -> om.getAnnotation(XmlElement.class).required()).orElse(false);
        } else if (f.isAnnotationPresent(XmlAttribute.class)
                || m.map(om -> om.isAnnotationPresent(XmlAttribute.class)).orElse(false)) {
            return Optional.ofNullable(f.getAnnotation(XmlAttribute.class)).map(XmlAttribute::required).orElse(false)
                    || m.map(om -> om.getAnnotation(XmlAttribute.class).required()).orElse(false);
        }

        return false;
    }

    public static Method searchMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
        try {
            return cls.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    public static Optional<Method> fieldGetter(Field f) {
        return fieldGetter(f.getDeclaringClass(), f);
    }

    public static Optional<Method> fieldGetter(Class<?> cls, Field f) {
        Optional<Method> m;
        try {
            m = Arrays.stream(Introspector.getBeanInfo(cls).getPropertyDescriptors())
                    .filter(pd -> pd.getReadMethod() != null && f.getName().equals(pd.getName())).findFirst()
                    .map(PropertyDescriptor::getReadMethod);
        } catch (IntrospectionException e) {
            m = Optional.empty();
        }
        return m;
    }

    public static Optional<Method> fieldSetter(Field f) {
        return fieldSetter(f.getDeclaringClass(), f);
    }

    public static Optional<Method> fieldSetter(Class<?> cls, Field f) {
        Optional<Method> m;
        try {
            m = Arrays.stream(Introspector.getBeanInfo(cls).getPropertyDescriptors())
                    .filter(pd -> pd.getWriteMethod() != null && f.getName().equals(pd.getName())).findFirst()
                    .map(PropertyDescriptor::getWriteMethod);
        } catch (IntrospectionException e) {
            m = Optional.empty();
        }
        return m;
    }

    public static Object getValue(Object obj, Field f) throws IllegalArgumentException, IllegalAccessException {
        Optional<Method> getter = fieldGetter(obj.getClass(), f);
        if (getter.isPresent()) {
            try {
                return getter.get().invoke(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return f.get(obj);
            }
        } else {
            return f.get(obj);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T mapFields(Object from, T to) {
        Arrays.stream(to.getClass().getDeclaredFields()).forEach(f -> {
            Arrays.stream(f.getAnnotationsByType(FieldMapping.class))
                    .filter(m -> m.from().isAssignableFrom(from.getClass())).forEach(m -> {
                        try {
                            ClassTools.trySetAccessible(f);
                            Class<? extends FieldMappingAdapter> aCls = m.adapter();
                            Constructor<?> constructor = aCls.getDeclaredConstructor();
                            ClassTools.trySetAccessible(constructor);
                            FieldMappingAdapter adapter = (FieldMappingAdapter) constructor.newInstance();
                            Field fromF = from.getClass().getDeclaredField(m.name());
                            ClassTools.trySetAccessible(fromF);
                            f.set(to, adapter.mapTo(fromF.get(from)));
                        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                                | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
        return to;
    }

    public static <T> String serializeObject(T dataObj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(dataObj);
            return new String(Base64.getEncoder().encode(bos.toByteArray()), StandardCharsets.UTF_8);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserializeObject(String dataStr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(
                Base64.getDecoder().decode(dataStr.getBytes(StandardCharsets.UTF_8)));
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bis);
            return (T) in.readObject();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cloneObject(Object obj) {
        try {
            Object clone = ClassTools.newInstance(obj.getClass(), false);
            for (Field field : obj.getClass().getDeclaredFields()) {
                ClassTools.trySetAccessible(field);
                field.set(clone, field.get(obj));
            }
            return (T) clone;
        } catch (Exception e) {
            return null;
        }
    }

    public static abstract class FieldMappingAdapter<ValueType, BoundType> {
        public abstract BoundType mapTo(ValueType v);
    }

}
