package com.github.abu.methodactivity.activity.domain;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;

@Data
public class ParsedActivity<T> {
    private String activity;
    private String entity;
    private String entityId;
    private T params;

    public static <T> T createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return (T) clazz.getDeclaredConstructor().newInstance();
    }
}
