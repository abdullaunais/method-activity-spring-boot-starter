package com.owlcitydev.activitypoc.activity.parser;

import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import org.aspectj.lang.ProceedingJoinPoint;

public interface IActivityParser {
    <T> ParsedActivity<T> parseActivity(String activityTemplate, ProceedingJoinPoint proceedingJoinPoint, Object returnObject);

    <T> ParsedActivity<T> parseActivity(String activityTemplate, ProceedingJoinPoint proceedingJoinPoint);
}
