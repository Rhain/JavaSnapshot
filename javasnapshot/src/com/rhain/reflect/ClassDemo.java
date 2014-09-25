package com.rhain.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rhain on 2014/9/25.
 */
public class ClassDemo {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List list1 = new ArrayList();

        List<String> list2 = new ArrayList<>();

        Class c1 = list1.getClass();
        Class c2 = list2.getClass();

        System.out.println(c1 == c2);
        /**
         * c1 == c2结果返回true说明编译之后集合是去泛型化的
         * Java中的泛型是防止错误输入的，只在编译阶段有效，绕过编译就无效了
         * 可以通过方法的放射来绕过编译
         */

        list2.add("Hello");
        //list2.add(2222);  此时添加是会报错的
        Method method = c2.getMethod("add",Object.class);
        method.invoke(list2,333);

        System.out.println(list2.size()); //输出 2
    }
}
