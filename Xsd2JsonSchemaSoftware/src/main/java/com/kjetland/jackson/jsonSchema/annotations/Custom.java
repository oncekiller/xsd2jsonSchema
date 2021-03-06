package com.kjetland.jackson.jsonSchema.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


//Interface personnalisé
@Target({ METHOD, FIELD, PARAMETER, TYPE })
@Retention(RUNTIME)
public @interface Custom {
    String name();
    String value() default "";
    String type() default "";
}

