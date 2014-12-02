package com.rhain.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Rhain
 * @since 2014/11/27.
 */
public class CacheFactory {

    private CacheFactory() {
    }

    private static CacheFactory instance;

    private HashMap<String, Class> registeredCaches = new HashMap<>();

    public static CacheFactory getInstance() {
        if (instance == null) {
            synchronized (CacheFactory.class) {
                if (instance == null) {
                    instance = new CacheFactory();
                }
            }
        }
        return instance;
    }

    public void setRegisteredCaches(String cacheName, Class clazz) {
        registeredCaches.put(cacheName, clazz);
    }

    public Instance getCacheInstance(String cacheName) {
        try {
            Class cacheClass = registeredCaches.get(cacheName);
            if (cacheClass != null) {
                Constructor cacheConstructor = cacheClass.getDeclaredConstructor();
                return (Instance) cacheConstructor.newInstance();
            }
            throw new RuntimeException("缓存不存在");
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
