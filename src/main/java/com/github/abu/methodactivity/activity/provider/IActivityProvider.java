package com.github.abu.methodactivity.activity.provider;

import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.activity.domain.ParsedActivity;

import java.lang.annotation.Annotation;

public interface IActivityProvider {

    void send(ParsedActivity<?> activity, Annotation annotation, ActivityLevel level);
}
