package com.rhain.annotation;

import java.lang.annotation.*;

/**
 * @author Rhain
 * @since 2014/11/21.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InheritedAnnotation {
}
