package com.github.abu.methodactivity.activity.annotations.activity;

import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.activity.domain.BaseActivityParams;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreActivity {
    String value();

    String entity() default "generic";

    String entityId() default "''";

    Class<?> paramClass() default BaseActivityParams.class;

    ActivityLevel level() default ActivityLevel.INFO;
}
