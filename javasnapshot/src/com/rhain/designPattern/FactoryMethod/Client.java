package com.rhain.designPattern.FactoryMethod;

/**
 * @author Rhain
 * @since 2014/10/8.
 */
public class Client {
    public static void main(String[] args) {
        Creater creater = new ConcreteCreator();
        creater.doSomething();
    }
}
