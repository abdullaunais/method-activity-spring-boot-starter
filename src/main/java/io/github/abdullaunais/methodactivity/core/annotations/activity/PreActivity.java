package io.github.abdullaunais.methodactivity.core.annotations.activity;

import io.github.abdullaunais.methodactivity.core.annotations.param.PreActivityParams;
import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreActivity {
    String value();

    String entity() default "generic";

    String entityId() default "''";

    Class<? extends PreActivityParams> paramClass() default PreActivityParams.class;

    ActivityLevel level() default ActivityLevel.INFO;
}
