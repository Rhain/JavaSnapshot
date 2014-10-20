package com.rhain.betterjava;

/**
 * @author Rhain
 * @since 2014/9/16.
 */
public class BuilderDataHolder {

    private final String data;
    private final int num;
    //more fields

    private BuilderDataHolder(String data, int num) {
        this.data = data;
        this.num = num;
    }

    public static class Builder{
        private String data;
        private int num;

        public Builder data(String data){
            this.data = data;
            return this;
        }

        public Builder num(int num){
            this.num = num;
            return this;
        }

        public BuilderDataHolder build(){
            return new BuilderDataHolder(data,num);
        }
    }
}
