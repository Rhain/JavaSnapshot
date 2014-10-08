package com.rhain.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Rhain on 2014/9/25.
 */
public class ClassUtil {

    /**
     * 打印类的信息，类成员函数，成员变量
     * @param obj
     */
    public static void printClassInfo(Object obj){
        //获取类类型  传的是哪个子类的类，就是该子类的类类型
        Class clazz = obj.getClass();

        //获取类名称
        System.out.println(clazz.getName());
        printMethodInfo(clazz);
        printFieldInfo(clazz);


    }

    public static void printFieldInfo(Object obj) {

        //Field 封装了类的成员变量

        Class clazz = obj.getClass();
        //后去所有public的成员变量
        Field[] fs1 = clazz.getFields();
        //得到该类自己的所有成员变量
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            //得到成员变量的类型
            Class type = f.getType();
            //得到成员变量的类型名称
            String typeName = type.getName();
            //得到成员变量名称
            String fieldName = f.getName();
            System.out.println(typeName+" " + fieldName);
        }
    }

    public static void printMethodInfo(Object obj) {
        Class clazz = obj.getClass();
        //获取所有public的方法包括父类的方法
        Method[] methods = clazz.getMethods();

        //获取所有该类自己声明的方法，不问访问权限
        //clazz.getDeclaredMethods();
        for (Method method : methods) {
            //得到返回值的类类型
            Class returnClass = method.getReturnType();
            System.out.print(returnClass.getName() + " ");
            //得到方法名
            System.out.print(method.getName() + "(");
            //获取参数类型
            Class[] paramClass = method.getParameterTypes();
            for (Class paramClas : paramClass) {
                System.out.print(paramClas.getName() + " ");
            }
            System.out.println(")");
        }
    }

    public static void printConstructorInfo(Object obj){
        Class clazz = obj.getClass();
        //得到所有的public的构造函数
        //Constructor[] cs1 = clazz.getConstructors();
        //得到自己所有的构造函数，
        Constructor[] cs = clazz.getDeclaredConstructors();
        for (Constructor c : cs) {
            System.out.print(c.getName() + "(");
            Class[] paramTypes = c.getParameterTypes();
            for (Class paramType : paramTypes) {
                String type = paramType.getName();
                System.out.print(type+",");
            }
            System.out.println(")");
        }
    }
}
