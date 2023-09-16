package io.github.abdullaunais.methodactivity.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({ActivityAutoConfiguration.class})
public @interface EnableMethodActivity {
    String value() default "";
}
