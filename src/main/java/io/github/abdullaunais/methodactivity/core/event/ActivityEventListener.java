package io.github.abdullaunais.methodactivity.core.event;

import io.github.abdullaunais.methodactivity.core.annotations.param.ErrorActivityParams;
import io.github.abdullaunais.methodactivity.core.annotations.param.PostActivityParams;
import io.github.abdullaunais.methodactivity.core.annotations.param.PreActivityParams;
import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;

public interface ActivityEventListener {

    void onPreActivity(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData);
    void onPostActivity(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData);
    void onErrorActivity(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData);
}
