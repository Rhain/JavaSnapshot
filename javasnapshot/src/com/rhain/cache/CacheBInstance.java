package com.rhain.cache;

import java.util.Random;

/**
 * @author Rhain
 * @since 2014/11/27.
 */
public class CacheBInstance extends Instance {

    static {
        CacheFactory.getInstance().setRegisteredCaches(CacheBInstance.CACHE_NAME, CacheBInstance.class);
    }

    public final static String CACHE_NAME = "cache2";

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getCache() {
        return "Interstealler!" + new Random().nextInt(1000);
    }
}
