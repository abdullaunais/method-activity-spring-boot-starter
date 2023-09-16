package io.github.abdullaunais.methodactivity.core.annotations.activity;

import io.github.abdullaunais.methodactivity.core.annotations.param.BaseActivityParams;
import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ErrorActivity {
    String value();

    String entity() default "generic";

    String entityId() default "''";

    Class<? extends BaseActivityParams> paramClass() default BaseActivityParams.class;

    ActivityLevel level() default ActivityLevel.ERROR;
}
