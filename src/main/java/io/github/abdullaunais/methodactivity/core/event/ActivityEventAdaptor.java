package io.github.abdullaunais.methodactivity.core.event;

import io.github.abdullaunais.methodactivity.autoconfigure.EnableMethodActivity;
import io.github.abdullaunais.methodactivity.core.configuration.ActivityConfiguration;
import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnBean(annotation = EnableMethodActivity.class)
public class ActivityEventAdaptor {
    private final ActivityConfiguration activityConfiguration;
    private final ListableBeanFactory listableBeanFactory;

    public ActivityEventAdaptor(ActivityConfiguration activityConfiguration, ListableBeanFactory listableBeanFactory) {
        this.activityConfiguration = activityConfiguration;
        this.listableBeanFactory = listableBeanFactory;
    }

    public void sendQualifiedEvent(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData) {
        Map<String, ActivityEventListener> beansOfType = listableBeanFactory.getBeansOfType(ActivityEventListener.class);
        beansOfType.values().stream()
                .filter(activityProvider -> annotationData.getLevel().toInt() >= activityConfiguration.getActivityLevel().toInt())
                .forEach(activityProvider -> {
                    switch (activityType) {
                        case PreActivity ->
                                activityProvider.onPreActivity(parsedActivity, annotationData);
                        case PostActivity ->
                                activityProvider.onPostActivity(parsedActivity, annotationData);
                        case ErrorActivity ->
                                activityProvider.onErrorActivity(parsedActivity, annotationData);
                        default -> throw new IllegalStateException("Unexpected activity type: " + activityType);
                    }
                });
    }

}
