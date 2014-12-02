package com.rhain.cache;

/**
 * @author Rhain
 * @since 2014/11/27.
 */
public class CacheCInstance extends Instance {

    static {
        CacheFactory.getInstance().setRegisteredCaches(CacheCInstance.CACHE_NAME, CacheCInstance.class);
    }

    public final static String CACHE_NAME = "forever";

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getCache() {
        return "Forever Love!";
    }
}
