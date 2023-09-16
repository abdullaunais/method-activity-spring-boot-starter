package com.owlcitydev.activitypoc.activity.provider;

import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import org.slf4j.event.Level;

import java.lang.annotation.Annotation;

public interface IActivityProvider {

    void send(ParsedActivity<?> activity, Annotation annotation, Level level);
}
