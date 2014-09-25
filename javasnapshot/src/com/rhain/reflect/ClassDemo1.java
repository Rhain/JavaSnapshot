package com.rhain.reflect;

/**
 * Created by Rhain on 2014/9/25.
 */
public class ClassDemo1 {
    public static void main(String[] args) {

        Foo foo = new Foo();

        //获取类的三种方式
        Class c1 = Foo.class;

        Class c2 = foo.getClass();

        System.out.println(c1 == c2);

        Class c3 = null;
        try {//动态加载类
            c3 = Class.forName("com.rhain.reflect.Foo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(c1 == c2);

        try {
            Foo foo2 = (Foo) c1.newInstance();
            foo2.print();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}

class Foo{
    void print(){
        System.out.println("Foo");
    }
}
