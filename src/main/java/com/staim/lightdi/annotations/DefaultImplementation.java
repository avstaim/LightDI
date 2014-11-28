package com.staim.lightdi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Default Implementation Annotation is used to provide default implementation of Interface supporting LightDI. (Required)
 *
 * Created by a_shcherbinin on 28.11.14.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultImplementation {
    Class<?> value();
}
