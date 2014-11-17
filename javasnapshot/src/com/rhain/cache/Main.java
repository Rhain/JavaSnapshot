package com.rhain.cache;

/**
 * @author rhain
 * @since 2014/11/14.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        /*
        String s = Cache.getCache("cache");
        String f = Cache.getCache("forever");
        System.out.println(s);
        System.out.println(f);
        Thread.currentThread().sleep(5000);
        String s2 = Cache.getCache("cache");
        String s3 = Cache.getCache("cache");
        String f2 = Cache.getCache("forever");
        System.out.println(s2);//s2跟s不一样，说明缓存reload了
        System.out.println(s3);//s3跟s2一样说明缓存了
        System.out.println(f2);//f2跟f一样，说明没有reload
        System.exit(0);
        */

        /**
         * Result:
         Big Bang!503
         Forever Love!
         Big Bang!912
         Big Bang!912
         Forever Love!
         */

        String s3 = Cache.getCache("cache");
        System.out.println(s3);
        Thread.currentThread().sleep(2000);
        String s5 = Cache.getCache("cache");
        System.out.println(s5);//s3 和s5 相同说明缓存了
        Cache.reload("cache");//手动刷新缓存
        String s4 = Cache.getCache("cache");
        System.out.println(s4);//s4 和s5、s3不同，说明手动刷新缓存成功了
        System.out.println("----------------------------------");
        String s6 = Cache.getCache("cache2");
        System.out.println(s6);
        Cache.reload("cache2");
        Thread.currentThread().sleep(3000); //在reload的缓存的时候，没有reload成功就会用以前的值，这就是为什么没有加上sleep（）
                                            //方法，得到的s6和s7的值是一样的。因为代码执行的太快，但是缓存还没有reload成功
        String s7 = Cache.getCache("cache2");
        System.out.println(s7);

        System.exit(0);
        /**
         * result：
         Big Bang!364
         Big Bang!364
         Big Bang!164
         */
    }
}
