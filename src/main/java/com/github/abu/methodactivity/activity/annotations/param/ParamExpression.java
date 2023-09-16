package com.github.abu.methodactivity.activity.annotations.param;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ParamExpression {
    String value();
}
