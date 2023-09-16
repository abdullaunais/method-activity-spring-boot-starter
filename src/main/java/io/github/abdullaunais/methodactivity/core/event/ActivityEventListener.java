package io.github.abdullaunais.methodactivity.core.event;

import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;

public interface ActivityEventListener {

    void onPreActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData);

    void onPostActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData);

    void onErrorActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData);
}
