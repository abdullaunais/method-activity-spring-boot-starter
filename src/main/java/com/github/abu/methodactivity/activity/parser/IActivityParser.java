package com.github.abu.methodactivity.activity.parser;

import com.github.abu.methodactivity.activity.domain.ActivityAnnotationData;
import com.github.abu.methodactivity.activity.domain.ParsedActivity;
import org.aspectj.lang.ProceedingJoinPoint;

public interface IActivityParser {
    <T> ParsedActivity<T> parseActivity(ActivityAnnotationData annotationData, ProceedingJoinPoint proceedingJoinPoint, Object returnObject, Long executionTime);

    <T> ParsedActivity<T> parseActivity(ActivityAnnotationData annotationData, ProceedingJoinPoint proceedingJoinPoint);
}
