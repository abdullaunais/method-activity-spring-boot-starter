package com.owlcitydev.activitypoc.activity.annotations.activity;

import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;
import com.owlcitydev.activitypoc.activity.domain.BaseActivityParams;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ErrorActivity {
    String value();

    String entity() default "generic";

    String entityId() default "''";

    Class<?> paramClass() default BaseActivityParams.class;

    ActivityLevel level() default ActivityLevel.ERROR;
}
