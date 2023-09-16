package io.github.abdullaunais.methodactivity.core.aspect;

import io.github.abdullaunais.methodactivity.core.annotations.activity.ErrorActivity;
import io.github.abdullaunais.methodactivity.core.annotations.activity.PostActivity;
import io.github.abdullaunais.methodactivity.core.annotations.activity.PreActivity;
import io.github.abdullaunais.methodactivity.autoconfigure.EnableMethodActivity;
import io.github.abdullaunais.methodactivity.core.annotations.param.BaseActivityParams;
import io.github.abdullaunais.methodactivity.core.annotations.param.ErrorActivityParams;
import io.github.abdullaunais.methodactivity.core.annotations.param.PostActivityParams;
import io.github.abdullaunais.methodactivity.core.annotations.param.PreActivityParams;
import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import io.github.abdullaunais.methodactivity.core.parser.IActivityParser;
import io.github.abdullaunais.methodactivity.core.event.ActivityEventAdaptor;
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
    private final ActivityEventAdaptor activityProviderAdaptor;
    private final IActivityParser activityParser;


    public ActivityAspect(ActivityEventAdaptor activityProviderAdaptor, IActivityParser activityParser) {
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
            log.trace("created annotation data for PreActivity");
            ActivityType activityType = ActivityType.PreActivity;
            PreActivityParams preActivityParams = preActivity.paramClass().getDeclaredConstructor().newInstance();
            ParsedActivity<PreActivityParams> activity = activityParser.parseActivity(annotationData, proceedingJoinPoint, preActivityParams);
            activityProviderAdaptor.sendQualifiedEvent(activity, activityType, annotationData);
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
            log.trace("created annotation data for PostActivity");
            ActivityType activityType = ActivityType.PostActivity;
            PostActivityParams postActivityParams = postActivity.paramClass().getDeclaredConstructor().newInstance();
            postActivityParams.setExecutionTime(executionTime);
            postActivityParams.setReturnObject(returnObject);
            ParsedActivity<PostActivityParams> activity = activityParser.parseActivity(annotationData, proceedingJoinPoint, postActivityParams);
            activityProviderAdaptor.sendQualifiedEvent(activity, activityType, annotationData);
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
        } catch (Exception t) {
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
                log.trace("created annotation data for ErrorActivity");
                ActivityType activityType = ActivityType.ErrorActivity;
                ErrorActivityParams errorActivityParams = errorActivity.paramClass().getDeclaredConstructor().newInstance();
                errorActivityParams.setException(t);
                ParsedActivity<ErrorActivityParams> activity = activityParser.parseActivity(annotationData, proceedingJoinPoint, errorActivityParams);
                activityProviderAdaptor.sendQualifiedEvent(activity, activityType, annotationData);
            } catch (Exception e) {
                log.error("Exception in errorActivity: ", e);
            }
            throw t;
        }
    }


}
