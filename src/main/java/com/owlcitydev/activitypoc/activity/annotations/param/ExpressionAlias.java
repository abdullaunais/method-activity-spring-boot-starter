package com.owlcitydev.activitypoc.activity.annotations.param;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExpressionAlias {
    String value();
}
