package com.rhain.reflect;

import java.lang.reflect.Method;

/**
 * Created by Rhain on 2014/9/25.
 */
public class MethodReflect {

    public static void main(String[] args) throws Exception {
        //方法的名称和方法的参数列表才能唯一确定某方法
        //方法的反射操作  method.invoke(对象,参数列表)

        //获取类类型
        A a = new A();
        Class c = a.getClass();

        //获取方法  名称和参数来决定
        //获取的是public的方法
        //Method m = c.getMethod("print",Integer.class,Integer.class)
        //获取的是自己声明的方法
        Method m = c.getDeclaredMethod("print",String.class,String.class);
        //方法的反射操作 方法没有返回值返回null，有返回值，返回具体的值
        Object o = m.invoke(a,"sssss","BBBBBB");


    }
}

class A{
    public void print(int a,int b){
        System.out.println(a+b);
    }
    public void print(String a,String b){
        System.out.println(a.toUpperCase()+b.toLowerCase());
    }
}