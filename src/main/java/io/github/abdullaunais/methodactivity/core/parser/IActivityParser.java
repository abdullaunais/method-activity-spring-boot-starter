package io.github.abdullaunais.methodactivity.core.parser;

import io.github.abdullaunais.methodactivity.core.annotations.param.BaseActivityParams;
import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import org.aspectj.lang.ProceedingJoinPoint;

public interface IActivityParser {
    <T extends BaseActivityParams> ParsedActivity<T> parseActivity(ActivityType activityType, ActivityAnnotationData annotationData, ProceedingJoinPoint proceedingJoinPoint, T activityParams);
}
