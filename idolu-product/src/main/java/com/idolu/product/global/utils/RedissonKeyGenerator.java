package com.idolu.product.global.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;

public class RedissonKeyGenerator {

    public static String generateDynamicKey(String identifier, Object[] args, Class<?> paramClassType, String[] parameterNames) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        String dynamicKey;
        if (paramClassType.equals(Object.class)) {
            dynamicKey = createDynamicKeyFromPrimitive(parameterNames, args, identifier);
        } else {
            dynamicKey = createDynamicKeyFromObject(args, paramClassType, identifier);
        }

        return dynamicKey;
    }

    private static String createDynamicKeyFromPrimitive(String[] parameterNames, Object[] args, String identifier) {
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(identifier)) {
                return String.valueOf(args[i]);
            }
        }

        throw new IllegalStateException();
    }

    private static String createDynamicKeyFromObject(Object[] args, Class<?> paramClassType, String identifier) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String paramClassName = paramClassType.getSimpleName();
        for (int i = 0; i < args.length; i++) {
            String argsClassName = args[i].getClass().getSimpleName();
            if (argsClassName.startsWith(paramClassName)) {
                Class<?> aClass = args[i].getClass();
                String capitalize = StringUtils.capitalize(identifier);
                Object result = aClass.getMethod("get" + capitalize).invoke(args[i]);
                return String.valueOf(result);
            }
        }

        throw new IllegalStateException();
    }
}
