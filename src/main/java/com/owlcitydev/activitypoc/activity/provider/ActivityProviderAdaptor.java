package com.owlcitydev.activitypoc.activity.provider;

import com.owlcitydev.activitypoc.activity.configuration.ActivityConfiguration;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import com.owlcitydev.activitypoc.activity.annotations.provider.ActivityProvider;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class ActivityProviderAdaptor {
    private final ActivityConfiguration activityConfiguration;

    public ActivityProviderAdaptor(ActivityConfiguration activityConfiguration) {
        this.activityConfiguration = activityConfiguration;
    }

    public void send(ParsedActivity<?> activity, Annotation annotation, Level level) {
        activityConfiguration.getRegisteredActivityProviders().stream()
                .filter(activityProvider -> activityProvider.getClass().isAnnotationPresent(ActivityProvider.class))
                .forEach(activityProvider -> activityProvider.send(activity, annotation, level));

    }

}
