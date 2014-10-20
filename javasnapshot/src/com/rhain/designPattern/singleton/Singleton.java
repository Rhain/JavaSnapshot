package com.rhain.designPattern.singleton;

/**
 * @author Rhain
 * @since 2014/9/12.
 */
public class Singleton {

    private static Singleton instance;

    private Singleton(){}

    public static synchronized Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public void doSomething(){
        //TODO
    }
}
