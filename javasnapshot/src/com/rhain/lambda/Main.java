package com.rhain.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Rhain
 * @since 2014/10/27.
 */
public class Main {

    public static void main(String[] args) {

        Person p1 = new Person("Jack");

        Person p2 = new Person("Rose");

        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);

        //一般的对Person的name进行排序代码
        Collections.sort(persons,new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });

        //使用lambda表达式，去除冗余的匿名类
        //Collections.sort(persons,(Person o1,Person o2) -> o1.getName().compareTo(o2.getName()));

        //借助Comparator的comparing方法进一步抽象
        //Collections.sort(persons,Comparator.comparing((Person p) -> p.getName()));

        //使用方法引用替代getName
        //Collections.sort(persons, Comparator.comparing(Person::getName));

        //给List接口增加默认方法sort()
        //persons.sort(Comparator.comparing(Person::getName));

        /*
        参见：http://zh.lucida.me/blog/java-8-lambdas-insideout-language-features/
        * */
    }
}
