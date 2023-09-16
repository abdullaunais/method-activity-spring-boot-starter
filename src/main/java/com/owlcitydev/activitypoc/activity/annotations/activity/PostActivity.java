package com.owlcitydev.activitypoc.activity.annotations.activity;

import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PostActivity {
    String value();

    ActivityLevel level() default ActivityLevel.INFO;
}
