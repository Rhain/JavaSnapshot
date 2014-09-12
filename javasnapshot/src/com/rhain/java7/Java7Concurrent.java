package com.rhain.java7;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LiuShaoQi
 * @since 2014/8/27.
 */
public class Java7Concurrent {

    private final AtomicInteger seq = new AtomicInteger(0);

    private static final int MAX_THREAD = 1000;

    public long nextId(){
        return seq.getAndIncrement();
    }

    private ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    public static class ProcessingThread extends Thread{
        private final String ident;
        private final CountDownLatch latch;

        public ProcessingThread(String ident,CountDownLatch latch){
            this.ident = ident;
            this.latch = latch;
        }

        public void init(){
            latch.countDown();
            System.out.println(latch.getCount());
        }

        public void run(){
            init();
        }
    }

    public static void main(String[] args) {
        final int quorum = 1 + (int)(MAX_THREAD/2);
        final CountDownLatch cdl = new CountDownLatch(quorum);

        final Set<ProcessingThread> nodes = new HashSet<>();
        long begin = System.currentTimeMillis();
        try{
            for(int i=0;i<MAX_THREAD;i++){
                ProcessingThread local = new ProcessingThread("localhost:"+i,cdl);
                nodes.add(local);
                local.start();
            }
            cdl.await();
            System.out.println("countdown");
            long end = System.currentTimeMillis();

            System.out.println("Take time:"+(end - begin));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

        }
    }
}
