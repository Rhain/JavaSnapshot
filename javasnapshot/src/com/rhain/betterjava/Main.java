package com.rhain.betterjava;

/**
 * @author Rhain
 * @since 2014/9/16.
 */
public class Main {

    public static void main(String[] args) {
        BuilderDataHolder bdh = new BuilderDataHolder.Builder()
                .data("Hello world")
                .num(2)
                .build();
    }
}
