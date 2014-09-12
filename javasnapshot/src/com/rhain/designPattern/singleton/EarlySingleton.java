package com.rhain.designPattern.singleton;

/**
 * @author LiuShaoQi
 * @since 2014/9/12.
 */
public class EarlySingleton {

    private static EarlySingleton instance = new EarlySingleton();

    private EarlySingleton(){}

    public static EarlySingleton getInstance(){
        return instance;
    }

    public void doSomething(){
        //TODO
    }
}
