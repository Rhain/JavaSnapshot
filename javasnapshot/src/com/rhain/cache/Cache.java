package com.rhain.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Rhain 集中管理缓存
 * @since 2014/11/14.
 */
public class Cache {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    private static LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(100, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {

        @Override
        public String load(String s) throws Exception {
            return randomStr(s);
        }

        @Override
        public ListenableFuture<String> reload(final String key, String oldValue) throws Exception {
            if (neverNeedsRefresh(key)) {
                return Futures.immediateCheckedFuture(oldValue);
            } else {
                ListenableFutureTask<String> task = ListenableFutureTask.create(new Callable<String>() {

                    @Override
                    public String call() throws Exception {
                        return randomStr(key);
                    }
                });
                executor.execute(task);
                return task;
            }
        }
    });

    private static String randomStr(String s) {
        Instance instance = CacheFactory.getInstance().getCacheInstance(s);
        if (instance != null) {
            return instance.getCache();
        }
        return null;
    }

    private static boolean neverNeedsRefresh(String key) {
        if (key.equals("forever")) {
            return true;
        }
        return false;
    }

    public static String getCache(String key) {
        try {
            return cache.getUnchecked(key);
        } catch (Exception e) {

        }
        return "\n" + " ((      /|_/|\n" + "  \\\\.._.'  , ,\\\n" + "  /\\ | '.__ v / \n" + " (_ .   /   \"         \n" + "  ) _)._  _ /\n" + " '.\\ \\|( / ( \n" + "   '' ''\\\\ \\\\";
    }

    public static void reload(String key) {
        cache.refresh(key);
    }
}
