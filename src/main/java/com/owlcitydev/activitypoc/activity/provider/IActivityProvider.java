package com.owlcitydev.activitypoc.activity.provider;

import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;

import java.lang.annotation.Annotation;

public interface IActivityProvider {

    void send(ParsedActivity<?> activity, Annotation annotation, ActivityLevel level);
}
