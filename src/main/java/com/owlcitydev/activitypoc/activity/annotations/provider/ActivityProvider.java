package com.owlcitydev.activitypoc.activity.annotations.provider;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ActivityProvider {
    String value() default "";
}
