package com.wa.edu.utils;

import cn.wa.common.tools.ReflectUtil;
import cn.wa.common.tools.StringUtil;
import groovy.lang.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by liupd on 2015/11/6.
 **/
public class ReflectConvertUtils {

    public static void removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    DefaultGroovyMethods.invokeMethod(list, "remove", new Object[]{j});
                }

            }

        }

    }

    public static List<Map> convertToList(List<Map> list) {
        List<Map> resultList = new ArrayList<Map>();
        if (list == null) {
            return resultList;
        }

        for (Map map : list) {
            ((ArrayList<Map>) resultList).add(convertToMap(map));
        }

        return resultList;
    }

    public static Map convertToMap(Map map) {
        Map resultMap = new HashMap();
        if (null != map) {
            Set keySet = map.keySet();
            for (int i = 0; i < keySet.size(); i++) {
                String key = (String) DefaultGroovyMethods.getAt(keySet, i);
                resultMap.put(keyConvert(key), map.get(key));
            }

        }

        return resultMap;
    }

    private static String keyConvert(String key) {
        String[] keyArr = key.split("_");
        StringBuffer str = new StringBuffer();
        try{
            for (int i = 0; i < keyArr.length; i++) {
                if (i != 0) {
                    str.append(toUpperCaseFirstOne(keyArr[i]));
                } else {
                    str.append(keyArr[i]);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        return str.toString();
    }

    private static String toUpperCaseFirstOne(String s) throws Exception{
        if (Character.isUpperCase(s.charAt(0))) return s;
        else return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    public static List<Object> convertList(Class clazz, List<Map> mapList)throws Exception{
        List<Object> list = new ArrayList<Object>();
        for (Map map : mapList) {
            ((ArrayList<Object>) list).add(convert(clazz, map));
        }
        return list;
    }

    /**
     * 将class转换成map并返回映射对象
     * @param clazz
     * @param map
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T convertToMap(Class clazz, Map map) throws Exception{
        T obj = (T) clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            StringBuffer dataField = new StringBuffer();
            String fieldName = f.getName();
            f.setAccessible(true);
            for (int i = 0; i < fieldName.length(); i++) {
                char c = fieldName.charAt(i);
                if (Character.isUpperCase(c) && i != 0 && i != fieldName.length() - 1) {
                    dataField.append("_");
                    dataField.append(Character.toLowerCase(c));
                } else {
                    dataField.append(c);
                }

            }

            Set keySet = map.keySet();
            if (keySet.contains(dataField.toString())) {
                f.set(obj, map.get(dataField.toString()));
            }
        }
        return obj;
    }


    /**
     * 将class转换成map并返回Object对象
     * @param clazz
     * @param map
     * @param Object
     * @return
     * @throws Exception
     */
    public static Object convert(Class clazz, Map map) throws Exception{
        Object obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            StringBuffer dataField = new StringBuffer();
            String fieldName = f.getName();
            f.setAccessible(true);
            for (int i = 0; i < fieldName.length(); i++) {
                char c = fieldName.charAt(i);
                if (Character.isUpperCase(c) && i != 0 && i != fieldName.length() - 1) {
                    dataField.append("_");
                    dataField.append(Character.toLowerCase(c));
                } else {
                    dataField.append(c);
                }
            }
            Set keySet = map.keySet();
            if (keySet.contains(dataField.toString())) {
                f.set(obj, map.get(dataField.toString()));
            }

        }

        return obj;
    }

    public static Object convert2(Class clazz, Map map) throws Exception{
        Object obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            StringBuffer dataField = new StringBuffer();
            String fieldName = f.getName();
            f.setAccessible(true);
            for (int i = 0; i < fieldName.length(); i++) {
                char c = fieldName.charAt(i);
                if (Character.isUpperCase(c) && i != 0 && i != fieldName.length() - 1) {
                    dataField.append("_");
                    dataField.append(Character.toLowerCase(c));
                } else {
                    dataField.append(c);
                }
            }
            Set keySet = map.keySet();
            if (keySet.contains(dataField.toString())) {
                if (map.get(dataField.toString()) != null) {
                    f.set(obj, map.get(dataField.toString()));
                }
            }
        }
        return obj;
    }


    public static Object objectFilter(Object obj, String filter) throws Exception{
        Set strSet = new HashSet();
        if (StringUtil.isNotNullStr(filter)) {
            CollectionUtils.addAll(strSet, filter.split(","));
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            String fieldName = f.getName();
            f.setAccessible(true);
            if (!strSet.contains(fieldName)) {
                f.set(obj, null);
            }
        }
        return obj;
    }

    /**
     * 对象之间属性copy，包含继承关系
     * @param src
     * @param des
     */
    public static void convert(Object src, Object des) throws  Exception{
        Class desclazz = des.getClass();
        Class srcclazz = src.getClass();
        Field[] fields = srcclazz.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            Method srcMethod = getMethods(srcclazz, fieldName);
            Object value = srcMethod.invoke(src);

            Field desField = getField(desclazz, fieldName);
            if (desField != null && value != null) {
                desField.setAccessible(true);
                desField.set(des, value);
                desField.setAccessible(false);
            }

        }

    }

    public static Field getField(Class srcclazz, String fieldName) {
        Field f = null;
        try {
            f = srcclazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            String name = srcclazz.getSuperclass().getName();
            if (DefaultGroovyMethods.contains(name, "com.wa.edu")) {
                return getField(srcclazz.getSuperclass(), fieldName);
            }
        }
        return f;
    }

    public static Method getMethods(Class srcclazz, String fieldName) {
        Method getMethod = null;
        try {
            Field f = srcclazz.getDeclaredField(fieldName);
            String isMethodName = "is" + StringUtils.capitalize(fieldName);
            String getMethodName = "get" + StringUtils.capitalize(fieldName);
            if (ReflectUtil.containsMethod(srcclazz, isMethodName)) {
                getMethod = srcclazz.getDeclaredMethod(isMethodName);
            } else if (ReflectUtil.containsMethod(srcclazz, getMethodName)) {
                getMethod = srcclazz.getDeclaredMethod(getMethodName);
            }

        } catch (Exception e) {
            return getMethods(srcclazz.getSuperclass(), fieldName);
        }

        return getMethod;
    }

    public static void convert2(Object src, Object des) {
        Class desclazz = des.getClass();
        Class srcclazz = src.getClass();
        try {
            convert2Class(src, srcclazz, des, desclazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convert2Class(Object src, Class srcclazz, Object des, Class desclazz) {
        Field[] fields = srcclazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            Method srcMethod = getMethods(srcclazz, fieldName);
            if (null != srcMethod) {
                Object value = null;
                try {
                    value = srcMethod.invoke(src);
                    Field desField = getField(desclazz, fieldName);
                    if (desField != null && value != null) {
                        desField.setAccessible(true);
                        desField.set(des, value);
                        desField.setAccessible(false);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        String name = srcclazz.getSuperclass().getName();
        if (DefaultGroovyMethods.contains(name, "com.wa.edu")) {
            convert2Class(src, srcclazz.getSuperclass(), des, desclazz);
        }

    }

    /**
     * 对象转map
     *
     * @param obj
     * @return
     */
    public static Map convertObjectToMap(Object obj) {
        Map fmap = new HashMap();
        convertObjectToMap2(obj, obj.getClass(), fmap);
        return fmap;
    }

    private static void convertObjectToMap2(Object src, Class srcclazz, Map fmap) {
        Field[] fields = srcclazz.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            Method srcMethod = getMethods(srcclazz, fieldName);
            if (null != srcMethod && !"errors".equals(fieldName)) {
                Object value = null;
                try {
                    value = srcMethod.invoke(src);
                    if (value != null) {
                        fmap.put(fieldName, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


        String name = srcclazz.getSuperclass().getName();
        if (DefaultGroovyMethods.contains(name, "com.wa.edu")) {
            convertObjectToMap2(src, srcclazz.getSuperclass(), fmap);
        }

    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Object convertMap2Object(Class type, Map map) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);// 获取类属性
        Object obj = type.newInstance();// 创建 JavaBean 对象
        Map proMap = convertMapKey(map);//转换Map Key

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (proMap.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                try {
                    Object value = proMap.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
        return obj;
    }

    private static Map convertMapKey(Map map) {
        //Java替换掉下划线并让紧跟它后面的字母大写
        final Map resMap = new HashMap();
        DefaultGroovyMethods.each(map, new Closure<Object>(null, null) {
            public Object doCall(Map.Entry<Object, Object> map) {
                StringBuffer sb = new StringBuffer((String) map.getKey());
                int count = sb.indexOf("_");
                while (count != 0) {
                    int num = sb.indexOf("_", count);
                    count = num + 1;
                    if (num != -1) {
                        char ss = sb.charAt(count);
                        char ia = (char) (ss - 32);
                        sb.replace(count, count + 1, String.valueOf(ia));
                    }
                }
                String resKey = sb.toString().replaceAll("_", "");
                return resMap.put(resKey, map.getValue());
            }
            public Object doCall() {
                return doCall(null);
            }

        });
        return resMap;
    }

}
