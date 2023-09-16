package com.owlcitydev.activitypoc.activity.annotations.activity;

import org.slf4j.event.Level;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreActivity {
    String value();

    Level level() default Level.INFO;
}
