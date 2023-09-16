package com.github.abu.methodactivity.test.providers;

import com.github.abu.methodactivity.activity.annotations.provider.ActivityProvider;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.activity.domain.ParsedActivity;
import com.github.abu.methodactivity.activity.provider.IActivityProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.lang.annotation.Annotation;

@Slf4j
@ActivityProvider
public class TestSlf4jActivityProvider implements IActivityProvider {

    @Override
    public void send(ParsedActivity<?> parsedActivity, Annotation annotation, ActivityLevel level) {
        log.atLevel(Level.intToLevel(level.toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        annotation.annotationType().getSimpleName(),
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
    }
}
