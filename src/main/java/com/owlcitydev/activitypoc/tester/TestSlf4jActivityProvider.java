package com.owlcitydev.activitypoc.tester;

import com.owlcitydev.activitypoc.activity.annotations.provider.ActivityProvider;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import com.owlcitydev.activitypoc.activity.provider.IActivityProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.lang.annotation.Annotation;

@Slf4j
@ActivityProvider
public class TestSlf4jActivityProvider implements IActivityProvider {

    @Override
    public void send(ParsedActivity<?> parsedActivity, Annotation annotation, Level level) {
        log.atLevel(level).log("({}) [{}]: {}", annotation.annotationType().getSimpleName(), parsedActivity.getParams(), parsedActivity.getActivity());
    }
}
