package com.rhain.designPattern.singleton;

/**
 * @author Rhain
 * @since 2014/9/12.
 */
public class LazySingleton {

    private static LazySingleton instance;

    private LazySingleton(){}

    public static LazySingleton getInstance(){
        if(instance == null){
            synchronized (LazySingleton.class){
                if(instance == null){
                    instance = new LazySingleton();
                }
            }
        }
        return  instance;
    }

    public void doSomething(){
        //TODO
    }
}
