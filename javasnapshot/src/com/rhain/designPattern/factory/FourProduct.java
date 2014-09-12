package com.rhain.designPattern.factory;

/**
 * @author LiuShaoQi
 * @since 2014/9/12.
 */
public class FourProduct implements Product {

    static {
        InstanceProductFactory.getInstance().regiterProduct("four",new FourProduct());
    }
}
