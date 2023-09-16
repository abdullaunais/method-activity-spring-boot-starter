package io.github.abdullaunais.methodactivity.autoconfigure;

import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import io.github.abdullaunais.methodactivity.core.event.ActivityEventListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@Slf4j
public class DefaultSlf4jLoggingActivityListener implements ActivityEventListener {

    @Override
    public void onPreActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        "PreActivity",
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
    }

    @Override
    public void onPostActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        "PostActivity",
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
    }

    @Override
    public void onErrorActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        "ErrorActivity",
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
    }
}
