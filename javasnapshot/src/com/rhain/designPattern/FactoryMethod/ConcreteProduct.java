package com.rhain.designPattern.FactoryMethod;

/**
 * @author Rhain
 * @since 2014/10/8.
 */
public class ConcreteProduct implements Product {
    @Override
    public void say() {
        System.out.println("Concrete Product");
    }
}
