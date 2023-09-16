package com.owlcitydev.activitypoc.activity.provider;

import com.owlcitydev.activitypoc.activity.annotations.provider.ActivityProvider;
import com.owlcitydev.activitypoc.activity.configuration.ActivityConfiguration;
import com.owlcitydev.activitypoc.activity.domain.ActivityAnnotationData;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class ActivityProviderAdaptor {
    private final ActivityConfiguration activityConfiguration;

    public ActivityProviderAdaptor(ActivityConfiguration activityConfiguration) {
        this.activityConfiguration = activityConfiguration;
    }

    public void send(ParsedActivity<?> activity, Annotation annotation, ActivityAnnotationData annotationData) {
        activityConfiguration.getRegisteredActivityProviders().stream()
                .filter(activityProvider -> activityProvider.getClass().isAnnotationPresent(ActivityProvider.class))
                .filter(activityProvider -> annotationData.getLevel().toInt() >= activityConfiguration.getActivityLevel().toInt())
                .forEach(activityProvider -> activityProvider.send(activity, annotation, annotationData.getLevel()));

    }

}
