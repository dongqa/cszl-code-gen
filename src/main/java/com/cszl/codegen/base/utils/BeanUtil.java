package com.cszl.codegen.base.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wdq
 */
public class BeanUtil {

    static {
        ConvertUtils.register(new DateConverter(null), Date.class);
    }

    /**
     * request生成bean对象
     *
     * @param request
     * @param beanClass
     * @return
     */
    public static <T> T requestToBean(HttpServletRequest request, Class<T> beanClass) {
        try {
            // 实例化随意类型
            T bean = beanClass.newInstance();
            return requestToBean(request, bean);
        } catch (Exception e) {
            // 如果错误 则向上抛运行时异常
            throw new RuntimeException(e);
        }
    }

    /**
     * request修改bean对象
     *
     * @param request
     * @param bean
     * @return
     */
    public static <T> T requestToBean(HttpServletRequest request, T bean) {
        try {
            // 获得参数的一个列举
            Enumeration<String> en = request.getParameterNames();
            // 遍历列举来获取所有的参数
            while (en.hasMoreElements()) {
                String name = en.nextElement();
                String value = request.getParameter(name);
                BeanUtils.setProperty(bean, name, value);
            }
            return bean;
        } catch (Exception e) {
            // 如果错误 则向上抛运行时异常
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToBean(JSONObject jsonObject, T bean) {
        List<Field> fields = getFields(bean.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String key = field.getName();
                if (jsonObject.containsKey(key)) {
                    BeanUtils.setProperty(bean, key, jsonObject.get(key));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * 只返回过滤的字段
     *
     * @param t
     * @param columnList
     * @param <T>
     * @return
     */
    public static <T> JSONObject beanToJSONObject(T t, List<String> columnList) {
        JSONObject jsonObject = new JSONObject();
        List<Field> fieldList = getFields(t.getClass());
        for (Field field : fieldList) {
            if (columnList.contains(field.getName())) {// 如果需要这个字段
                try {
                    field.setAccessible(true);
                    Object value = field.get(t);
                    if (value != null) {
                        jsonObject.put(field.getName(), value);
                    }
                    field.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    private static <T> List<Field> getFields(Class<T> class1) {
        Field[] fields = class1.getDeclaredFields();
        List<Field> fieldList = new ArrayList<Field>(Arrays.asList(fields));
        if (class1.getSuperclass() != Object.class) {
            fieldList.addAll(getFields(class1.getSuperclass()));
        }
        return fieldList;
    }

    public static Map<String, Object> beanToMap(Object object) {
        Method[] methods = object.getClass().getMethods();
        List<Method> get = Arrays.asList(methods).parallelStream().peek(m -> m.setAccessible(true)).filter(f -> f.getName().startsWith("get")).collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        for (Method m : get) {
            String name = m.getName().substring(3, m.getName().length());
            String s = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            Function<Method, Object> f = a -> {
                try {
                    return m.invoke(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            };
            returnMap.put(s, f.apply(m));
        }
        return returnMap;
    }
}
