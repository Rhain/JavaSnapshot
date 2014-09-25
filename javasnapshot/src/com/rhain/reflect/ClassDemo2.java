package com.rhain.reflect;

/**
 * Created by Rhain on 2014/9/25.
 */
public class ClassDemo2 {
    public static void main(String[] args) {
        Class iClass = int.class;
        Class IClass = Integer.class;

        Class VClass = Void.class;
        Class vClass = void.class;

        System.out.println(iClass.getName());
        System.out.println(IClass.getName());

        System.out.println(VClass.getSimpleName());
        System.out.println(vClass.getSimpleName());

        //ClassUtil.printClassInfo("string");
        //ClassUtil.printFieldInfo(new Integer(33));
        ClassUtil.printConstructorInfo("sss");
    }
}
