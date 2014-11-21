package com.rhain.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Rhain
 * @since 2014/11/21.
 */
public class Main {

    public static void main(String[] args) {
        Class<AnnotatedClass> object = AnnotatedClass.class;
        Annotation[] annotations = object.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

        if (object.isAnnotationPresent(CustomAnnotationClass.class)) {
            Annotation annotation = object.getAnnotation(CustomAnnotationClass.class);
            System.out.println(annotation);
        }

        for (Method method : object.getDeclaredMethods()) {
            if (method.isAnnotationPresent(CustomAnnotationMethod.class)) {
                Annotation annotation = method.getAnnotation(CustomAnnotationMethod.class);
                System.out.println(annotation);
            }
        }
    }
}
