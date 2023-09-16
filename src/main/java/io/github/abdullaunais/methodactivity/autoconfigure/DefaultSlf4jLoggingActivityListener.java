package io.github.abdullaunais.methodactivity.autoconfigure;

import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import io.github.abdullaunais.methodactivity.core.event.IActivityEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@Slf4j
public class DefaultSlf4jLoggingActivityListener implements IActivityEvent {

    @Override
    public void onEvent(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        activityType.name(),
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
    }
}
