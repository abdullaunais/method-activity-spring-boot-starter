package io.github.abdullaunais.methodactivity.core.event;

import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;

public interface IActivityEvent {

    void onEvent(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData);
}
