package com.github.abu.methodactivity.activity.aspect;

import com.github.abu.methodactivity.activity.annotations.activity.ErrorActivity;
import com.github.abu.methodactivity.activity.annotations.activity.PostActivity;
import com.github.abu.methodactivity.activity.annotations.activity.PreActivity;
import com.github.abu.methodactivity.activity.annotations.configuration.EnableMethodActivity;
import com.github.abu.methodactivity.activity.domain.ActivityAnnotationData;
import com.github.abu.methodactivity.activity.domain.ParsedActivity;
import com.github.abu.methodactivity.activity.parser.IActivityParser;
import com.github.abu.methodactivity.activity.provider.ActivityProviderAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@ConditionalOnBean(annotation = EnableMethodActivity.class)
public class ActivityAspect {
    private final ActivityProviderAdaptor activityProviderAdaptor;
    private final IActivityParser activityParser;


    public ActivityAspect(ActivityProviderAdaptor activityProviderAdaptor, IActivityParser activityParser) {
        this.activityProviderAdaptor = activityProviderAdaptor;
        this.activityParser = activityParser;
    }


    @Pointcut("@annotation(preActivity)")
    public void preActivityAnnotationPointcut(PreActivity preActivity) {
    }

    @Pointcut("@annotation(postActivity)")
    public void postActivityAnnotationPointcut(PostActivity postActivity) {
    }

    @Pointcut("@annotation(errorActivity)")
    public void errorActivityAnnotationPointcut(ErrorActivity errorActivity) {
    }

    @Around(value = "preActivityAnnotationPointcut(preActivity)", argNames = "proceedingJoinPoint,preActivity")
    public Object preActivity(ProceedingJoinPoint proceedingJoinPoint, PreActivity preActivity) throws Throwable {
        log.trace("invoked ActivityAspects.preActivity");
        try {
            ActivityAnnotationData annotationData = ActivityAnnotationData.builder()
                    .withTemplate(preActivity.value())
                    .withEntity(preActivity.entity())
                    .withEntityId(preActivity.entityId())
                    .withParamClass(preActivity.paramClass())
                    .withLevel(preActivity.level())
                    .build();
            ParsedActivity<?> activity = activityParser.parseActivity(annotationData, proceedingJoinPoint);
            activityProviderAdaptor.send(activity, preActivity, annotationData);
        } catch (Exception e) {
            log.error("Exception in preActivity: ", e);
        }
        return proceedingJoinPoint.proceed();
    }

    @Around(value = "postActivityAnnotationPointcut(postActivity)", argNames = "proceedingJoinPoint,postActivity")
    public Object postActivity(ProceedingJoinPoint proceedingJoinPoint, PostActivity postActivity) throws Throwable {
        log.trace("invoked ActivityAspects.postActivity");
        long start = System.currentTimeMillis();
        Object returnObject = proceedingJoinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        try {
            ActivityAnnotationData annotationData = ActivityAnnotationData.builder()
                    .withTemplate(postActivity.value())
                    .withEntity(postActivity.entity())
                    .withEntityId(postActivity.entityId())
                    .withParamClass(postActivity.paramClass())
                    .withLevel(postActivity.level())
                    .build();

            ParsedActivity<?> activity = activityParser.parseActivity(annotationData, proceedingJoinPoint, returnObject, executionTime);
            activityProviderAdaptor.send(activity, postActivity, annotationData);
        } catch (Exception e) {
            log.error("Exception in postActivity: ", e);
        }
        return returnObject;
    }

    @Around(value = "errorActivityAnnotationPointcut(errorActivity)", argNames = "proceedingJoinPoint,errorActivity")
    public Object errorActivity(ProceedingJoinPoint proceedingJoinPoint, ErrorActivity errorActivity) throws Throwable {
        log.trace("invoked ActivityAspects.errorActivity");
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            log.error("Error when proceeding joint point: {}", t.getMessage());
            log.trace("Error when proceeding joint point: ", t);
            log.error("Possibly an expected error, since error logging activity is fired");
            try {
                ActivityAnnotationData annotationData = ActivityAnnotationData.builder()
                        .withTemplate(errorActivity.value())
                        .withEntity(errorActivity.entity())
                        .withEntityId(errorActivity.entityId())
                        .withParamClass(errorActivity.paramClass())
                        .withLevel(errorActivity.level())
                        .build();

                ParsedActivity<?> activity = activityParser.parseActivity(annotationData, proceedingJoinPoint);
                activityProviderAdaptor.send(activity, errorActivity, annotationData);
            } catch (Exception e) {
                log.error("Exception in errorActivity: ", e);
            }
            throw t;
        }
    }


}
