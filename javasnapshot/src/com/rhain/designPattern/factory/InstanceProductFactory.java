package com.rhain.designPattern.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rhain
 * @since 2014/9/12.
 */
public class InstanceProductFactory {
    private  static InstanceProductFactory instance = new InstanceProductFactory();

    private InstanceProductFactory(){}

    public static InstanceProductFactory getInstance(){
        return instance;
    }

    private Map<String,Product> registerProducts = new HashMap<>();

    public void regiterProduct(String productId,Product product){
        registerProducts.put(productId, product);
    }

    public Product createProduct(String productId){
        return registerProducts.get(productId);
    }

}
