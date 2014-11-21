package com.rhain.annotation;

/**
 * @author Rhain
 * @since 2014/11/21.
 */
public class InheritedMain {

    public static void main(String[] args) {
        System.out.println("super class:" + AnnotatedSuperClass.class.isAnnotationPresent(InheritedAnnotation.class));
        System.out.println("sub class:" + AnnotatedSubClass.class.isAnnotationPresent(InheritedAnnotation.class));

        System.out.println("interface:" + AnnotatedInterface.class.isAnnotationPresent(InheritedAnnotation.class));
        System.out.println("implement interface:" + AnnotatedImplementedClass.class.isAnnotationPresent(InheritedAnnotation.class));
    }

}
