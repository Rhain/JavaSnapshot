package com.rhain.designPattern.FactoryMethod;

/**
 * @author Rhain
 * @since 2014/10/8.
 */
public abstract class Creater {

    public void doSomething(){
        Product product = getProduct();
        product.say();
    }

    protected abstract  Product getProduct();
}
