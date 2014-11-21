package com.rhain.annotation;

/**
 * @author Rhain
 * @since 2014/11/21.
 */
@CustomAnnotationClass(date = "2014-11-21")
public class AnnotatedClass {

    @CustomAnnotationMethod(date = "2014-11-21", description = "annotated method")
    public String annotatedMethod() {
        return "Hi,hao";
    }

    @CustomAnnotationMethod(author = "YiFei", date = "2014-11-21", description = "en,eng")
    public String anotherAnnotatedMethod() {
        return "Hi,yiFei";
    }
}
