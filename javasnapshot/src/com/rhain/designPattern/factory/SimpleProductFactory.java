package com.rhain.designPattern.factory;

/**
 * @author Rhain
 * @since 2014/9/12.
 */
public class SimpleProductFactory {

    private static SimpleProductFactory instance = new SimpleProductFactory();

    private SimpleProductFactory() {
    }

    public static SimpleProductFactory getInstance() {
        return instance;
    }

    public Product createProduct(String productId) {
        if ("ID1".equals(productId)) {
            return new OneProduct();
        }
        if ("ID2".equals(productId)) {
            return new TwoProduct();
        }
        return new EmptyProduct(); // 使用一个空对象来代替返回null值，这样就可以减少在代码中判断对象是否为null的判断，代码更好看
    }
}
