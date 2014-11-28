package com.staim.lightdi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses Internal Injection Annotation is used to mark class as containing fields marked with Inject
 *
 * Created by a_shcherbinin on 28.11.14.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesInternalInjection {}
