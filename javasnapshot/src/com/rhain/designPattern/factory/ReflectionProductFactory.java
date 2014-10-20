package com.rhain.designPattern.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Rhain
 * @since 2014/9/12.
 */
public class ReflectionProductFactory {

    private static ReflectionProductFactory instance = new ReflectionProductFactory();

    private ReflectionProductFactory(){}

    public static ReflectionProductFactory getInstance(){
        return instance;
    }

    private HashMap<String,Class> registeredProducts = new HashMap<>();

    public void registerProduct(String prodcutId,Class productClass){
        registeredProducts.put(prodcutId,productClass);
    }

    public Product createProduct(String productId){
        try {
            Class productClass = registeredProducts.get(productId);
            Constructor productConstructor = productClass.getDeclaredConstructor(new Class[]{String.class});
            return (Product) productConstructor.newInstance(new Object[]{});
        } catch (NoSuchMethodException |InvocationTargetException| InstantiationException| IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
