package io.github.abdullaunais.methodactivity.core.annotations.activity;

import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;
import io.github.abdullaunais.methodactivity.core.annotations.param.PostActivityParams;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PostActivity {
    String value();

    String entity() default "generic";

    String entityId() default "''";

    Class<? extends PostActivityParams> paramClass() default PostActivityParams.class;

    ActivityLevel level() default ActivityLevel.INFO;
}
