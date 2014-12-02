package com.rhain.cache;

import java.util.Random;

/**
 * @author Rhain
 * @since 2014/11/27.
 */
public class CacheAInstance extends Instance {

    static {
        CacheFactory.getInstance().setRegisteredCaches(CacheAInstance.CACHE_NAME, CacheAInstance.class);
    }

    public final static String CACHE_NAME = "cache";

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getCache() {
        return "Big Bang!" + new Random().nextInt(1000);
    }
}
