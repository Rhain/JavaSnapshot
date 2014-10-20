package com.rhain.designPattern.FactoryMethod;

/**
 * @author Rhain
 * @since 2014/10/8.
 */
public class ConcreteCreator extends Creater {
    @Override
    protected Product getProduct() {
        return new ConcreteProduct();
    }
}
