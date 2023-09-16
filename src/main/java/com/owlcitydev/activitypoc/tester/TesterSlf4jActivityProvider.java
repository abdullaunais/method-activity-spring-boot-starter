package com.owlcitydev.activitypoc.tester;

import com.owlcitydev.activitypoc.activity.annotations.provider.ActivityProvider;
import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import com.owlcitydev.activitypoc.activity.provider.IActivityProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.lang.annotation.Annotation;

@Slf4j
@ActivityProvider
public class TesterSlf4jActivityProvider implements IActivityProvider {

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
