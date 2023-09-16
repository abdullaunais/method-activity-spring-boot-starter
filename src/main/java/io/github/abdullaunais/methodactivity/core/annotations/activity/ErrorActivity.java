package io.github.abdullaunais.methodactivity.core.annotations.activity;

import io.github.abdullaunais.methodactivity.core.annotations.param.ErrorActivityParams;
import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;
import io.github.abdullaunais.methodactivity.core.annotations.param.PostActivityParams;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ErrorActivity {
    String value();

    String entity() default "generic";

    String entityId() default "''";

    Class<? extends ErrorActivityParams> paramClass() default ErrorActivityParams.class;

    ActivityLevel level() default ActivityLevel.ERROR;
}
