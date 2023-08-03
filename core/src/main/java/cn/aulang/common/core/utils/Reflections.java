package cn.aulang.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 反射的Utils函数集合.
 * <p>
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Utils函数.
 *
 * @author zhuchanglin
 */
@Slf4j
public class Reflections {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     * 支持层次结构,如dept.name，但不支持延迟加载的POJO对象
     *
     * @param object    Bean对象
     * @param fieldName 字段名称，支持嵌套属性，例如dept.name
     * @return bean中的属性值    属性不存在时，抛出@{code IllegalArgumentException}异常
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        return getFieldValue(object, fieldName, true);
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     * 支持层次结构,如dept.name，但不支持延迟加载的POJO对象
     *
     * @param object                Bean对象
     * @param fieldName             字段名称，支持嵌套属性，例如dept.name
     * @param exceptionWhenNotExist 当属性或map的key不存在时，是否抛出异常，true 抛出 false 返回null值
     * @return bean中的属性值
     */
    public static Object getFieldValue(final Object object, final String fieldName, boolean exceptionWhenNotExist) {
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            if (!map.containsKey(fieldName)) {
                if (exceptionWhenNotExist) {
                    throw new IllegalArgumentException(
                            "Could not find field [" + fieldName + "] on target [" + object + "]");
                } else {
                    return null;
                }
            }
            return map.get(fieldName);
        }

        String curFieldName = fieldName;
        int pos = fieldName.indexOf('.');
        String destFieldName = "";
        if (pos > -1) {
            curFieldName = fieldName.substring(0, pos);
            destFieldName = fieldName.substring(pos + 1);
        }

        Field field = getDeclaredField(object, curFieldName);
        if (field == null) {
            if (exceptionWhenNotExist) {
                throw new IllegalArgumentException(
                        "Could not find field [" + fieldName + "] on target [" + object + "]");
            } else {
                return null;
            }
        }
        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException ignore) {
            //ignore
        }
        if (pos > -1) {
            return Reflections.getFieldValue(result, destFieldName, exceptionWhenNotExist);
        } else {
            return result;
        }
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     * 支持层次结构,如dept.name，但不支持延迟加载的POJO对象
     *
     * @param object    Bean对象
     * @param fieldName 字段名称，支持嵌套属性，例如dept.name
     * @return bean中的属性值，值不存在时返回null
     */
    public static Object getFieldValueNoException(final Object object, final String fieldName) {
        return getFieldValue(object, fieldName, false);
    }

    /**
     * 获取符合POJO规范的对象的属性值，经过getter函数
     */
    public static Object getProperty(final Object object, final String fieldName) {
        try {
            return PropertyUtils.getProperty(object, fieldName);
        } catch (Exception e) {
            log.warn("获取{}对象的属性值{}失败，{}", object.getClass(), fieldName, e.getMessage());
        }
        return "";
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName,
                                     final Object value, boolean exceptionWhenNotExist) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            if (exceptionWhenNotExist) {
                throw new IllegalArgumentException(
                        "Could not find field [" + fieldName + "] on target [" + object + "]");
            }
            return;
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException ignore) {
            //ignore
        }
    }

    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        setFieldValue(object, fieldName, value, true);
    }

    public static void setFieldValueNoException(final Object object, final String fieldName, final Object value) {
        setFieldValue(object, fieldName, value, false);
    }

    /**
     * 直接调用对象方法,无视private/protected修饰符.
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters)
            throws InvocationTargetException {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        return invoke(object, method, parameters);
    }

    public static Object invoke(Object object, Method method, Object[] parameters) throws InvocationTargetException {
        method.setAccessible(true);
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException ignore) {
            //ignore
        }
        return null;
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        Assert.notNull(object, "object不能为空");
        Assert.hasText(fieldName, "fieldName");

        for (Class<?> superClass = object.getClass(); superClass != Object.class;
             superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    private static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        Assert.notNull(object, "object不能为空");

        for (Class<?> superClass = object.getClass(); superClass != Object.class;
             superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException ignore) {
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射,获得Class定义中声明的父类的第一个泛型参数的类型.
     * eg.
     * {@code public UserDao extends HibernateDao<User>}
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or {@code Object.class} when cannot be determined
     */
    public static Class<?> getSuperClassGenericType(Class<?> clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类指定次序的泛型参数的类型.
     * <p>
     * 如{@code public UserDao extends HibernateDao<User,Long>}
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic declaration, start from 0.
     * @return the index generic declaration, or {@code Object.class} when cannot be determined
     */
    public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            log.warn("{}'s superclass not ParameterizedType", clazz.getSimpleName());
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.warn("Index: {}, size of {}'s parameterized type: {}", index, clazz.getSimpleName(), params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn("{} not set the actual class on superclass generic parameter", clazz.getSimpleName());
            return Object.class;
        }
        return (Class<?>) params[index];
    }

    /**
     * 获取Bean的某属性的目标数据类型，如果是放在容器中，则解开返回容器元素的泛型类型
     *
     * @param clazz        原始的类
     * @param propertyName 成员属性名
     * @return 返回实际描述该属性的数据结构类型，例如
     * <pre>
     * {@code
     *   class A {
     *      String s;
     *      List<User> users;
     *   }
     * }
     * </pre>
     * {@code getActualTargetClass(A.class, "s")} 返回 {@code String.class}
     * <p>
     * {@code getActualTargetClass(A.class, "users")} 返回{@code User.class}
     */
    public static Class<?> getActualTargetClass(Class<?> clazz, String propertyName) {
        //如果是容器，则取出第一个泛型参数作为后续属性的宿主类，如List<Role> roles的第一个泛型是Role，它只有一个泛型参数。
        Class<?> objectType = getPropertyType(clazz, propertyName);
        if (Collection.class.isAssignableFrom(objectType)) {
            objectType = getPropertyArgumentsType(clazz, propertyName, 0);
        }
        return objectType;
    }

    /**
     * 通过反射获得Bean的某属性声明的第一个泛型类型参数
     */
    public static Class<?> getPropertyArgumentsType(Class<?> clazz, String propertyName) {
        return getPropertyArgumentsType(clazz, propertyName, 0);
    }

    /**
     * 通过反射获得Bean属性声明的泛型参数类型
     * 例如，User中的roles属性定义为,{@code Set<Role> roles}，则通过
     * {@code getPropertyArgumentsType(User.class, "roles", 0)}即可获得{@code Role.class}
     *
     * @param index 指定要获取从0开始的哪一个泛型参数
     */
    public static Class<?> getPropertyArgumentsType(Class<?> clazz, String propertyName, int index) {
        try {
            Field field = clazz.getDeclaredField(propertyName);
            Type genericType = field.getGenericType();
            if (!(genericType instanceof ParameterizedType)) {
                log.warn("{}'s property {} is not ParameterizedType", clazz.getSimpleName(), propertyName);
                return Object.class;
            }

            Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();
            if (index >= params.length || index < 0) {
                log.warn("Index: {}, size of {}'s property {} parameterized type: {}",
                        index, clazz.getSimpleName(), propertyName, params.length);
                return Object.class;
            }
            if (!(params[index] instanceof Class)) {
                log.warn("{}'s property {} not set the actual class on property type generic parameter",
                        clazz.getSimpleName(), propertyName);
                return Object.class;
            }
            return (Class<?>) params[index];
        } catch (SecurityException e) {
            log.error("Security Error in getPropertyArgumentsType", e);
        } catch (NoSuchFieldException e) {
            log.error("Filed {} not found in {}", propertyName, clazz.getSimpleName());
        }
        return Object.class;
    }

    /**
     * 提取集合中的对象的属性(通过getter函数),组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     */
    public static List<Object> fetchElementPropertyToList(Collection<?> collection, String propertyName) {
        List<Object> list = new ArrayList<>();
        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw convertToUncheckedException(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     */
    public static String fetchElementPropertyToString(Collection<?> collection, String propertyName, String separator) {
        List<Object> list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换字符串类型到clazz的property类型的值.
     *
     * @param value        待转换的字符串
     * @param clazz        提供类型信息的Class
     * @param propertyName 提供类型信息的Class的属性，支持嵌套属性，如dept.name
     */
    public static Object convertValue(Object value, Class<?> clazz, String propertyName) {
        try {
            if (StringUtils.contains(propertyName, ".")) {
                String destPropertyName = StringUtils.substringAfter(propertyName, ".");
                String destObjectName = StringUtils.substringBefore(propertyName, ".");
                Class<?> objectType = getActualTargetClass(clazz, destObjectName);

                return convertValue(value, objectType, destPropertyName);
            }

            Class<?> objectType = getActualTargetClass(clazz, propertyName);

            ConvertUtils.register(createDateConverter(), Date.class);
            return ConvertUtils.convert(value, objectType);
        } catch (Exception e) {
            throw convertToUncheckedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertValue(Object value, Class<T> clazz) {
        ConvertUtils.register(createDateConverter(), Date.class);
        if (value == null) {
            return null;
        }
        return (T) ConvertUtils.convert(value, clazz);
    }

    private static DateConverter createDateConverter() {
        DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(DatePattern.GENERAL_DATETIME_PATTERNS);
        return dc;
    }

    /**
     * 获得指定类型中，名称为propertyName的属性对应的类型
     * 使用PropertyUtils而不是直接new一个PropertyDescriptor的原因是借助其cache提升性能
     *
     * @param clazz        要查找的类型
     * @param propertyName 属性名
     * @return 属性对应的类型
     */
    public static Class<?> getPropertyType(Class<?> clazz, String propertyName) {
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor descriptor : descriptors) {
            if (propertyName.equals(descriptor.getName())) {
                return descriptor.getPropertyType();
            }
        }
        throw new RuntimeException("Can't find property: " + propertyName + " in class " + clazz);
    }

    /**
     * 返回被CGLIB代理的类的实际类型，即开发人员原始创建的类型
     */
    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    private static IllegalArgumentException convertToUncheckedException(Exception e) {
        if (e instanceof IllegalAccessException
                || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } else {
            return new IllegalArgumentException(e);
        }
    }
}