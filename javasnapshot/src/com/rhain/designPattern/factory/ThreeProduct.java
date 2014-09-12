package com.rhain.designPattern.factory;

/**
 * @author LiuShaoQi
 * @since 2014/9/12.
 */
public class ThreeProduct implements Product {

    //在每次添加新product的注册到工厂中，就不用修改工厂类了
    static {
        ReflectionProductFactory.getInstance().registerProduct("three",ThreeProduct.class);
    }
}
