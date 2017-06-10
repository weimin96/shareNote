package com.aoliao.notebook.utils.helper;

import com.aoliao.notebook.xmvp.XContract;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by 你的奥利奥 on 2017/5/14.
 */

public class GenericHelper {

    public static <T> Class<T> getViewClass(Class<?> klass) {
        Type type = klass.getGenericSuperclass();
        if(type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type t : types) {
            if (isPresenter(t)) {
                return (Class<T>) t;
            }
        }

        return null;
//        if(types == null || types.length == 0) return null;
//        return (Class<T>) types[0];
    }

    private static boolean isPresenter(Type t) {
        Class<?> aClass = (Class<?>) t;
        Class<?>[] classes = aClass.getInterfaces();
        for (Class<?> c : classes) {
            return c == XContract.Presenter.class || isPresenter(c);
        }
        return false;
    }

    public static  <T> T initPresenter(Object obj) {
        try {
            Class<?> currentClass = obj.getClass();
            Constructor construct = getViewClass(currentClass).getConstructor(getViewInterface(currentClass));
            return  (T) construct.newInstance(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("instance of presenter fail\n" +
                " Remind presenter need to extends XBasePresenter");
    }


    public static Class<?> getViewInterface(Class currentClass) {
        Class<?>[] classes = currentClass.getInterfaces();
        for (Class<?> c : classes) {
            if (c != XContract.View.class) {
                if (getViewInterface(c) == XContract.View.class) {
                    return c;
                }
            }
            return c;
        }
        throw new RuntimeException("no implement XContract.BaseView");
    }
}
