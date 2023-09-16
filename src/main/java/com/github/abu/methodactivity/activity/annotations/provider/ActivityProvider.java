package com.github.abu.methodactivity.activity.annotations.provider;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ActivityProvider {
    String value() default "";
}