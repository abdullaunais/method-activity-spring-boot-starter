package com.owlcitydev.activitypoc.activity.aspect;

import com.owlcitydev.activitypoc.activity.annotations.activity.ErrorActivity;
import com.owlcitydev.activitypoc.activity.annotations.activity.PostActivity;
import com.owlcitydev.activitypoc.activity.annotations.activity.PreActivity;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import com.owlcitydev.activitypoc.activity.parser.IActivityParser;
import com.owlcitydev.activitypoc.activity.provider.ActivityProviderAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ActivityAspects {
    private final ActivityProviderAdaptor activityProviderAdaptor;
    private final IActivityParser activityParser;


    public ActivityAspects(ActivityProviderAdaptor activityProviderAdaptor, IActivityParser activityParser) {
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
            String activityTemplate = preActivity.value();
            Level activityLevel = preActivity.level();
            log.debug("activityTemplate: {}", activityTemplate);
            ParsedActivity<?> activity = activityParser.parseActivity(activityTemplate, proceedingJoinPoint);
            activityProviderAdaptor.send(activity, preActivity, activityLevel);
        } catch (Exception e) {
            log.error("Exception in preActivity: ", e);
        }
        return proceedingJoinPoint.proceed();
    }

    @Around(value = "postActivityAnnotationPointcut(postActivity)", argNames = "proceedingJoinPoint,postActivity")
    public Object postActivity(ProceedingJoinPoint proceedingJoinPoint, PostActivity postActivity) throws Throwable {
        log.trace("invoked ActivityAspects.postActivity");
        Object returnObject = proceedingJoinPoint.proceed();
        try {
            String activityTemplate = postActivity.value();
            Level level = postActivity.level();
            log.debug("activityTemplate: {}", activityTemplate);
            ParsedActivity<?> activity = activityParser.parseActivity(activityTemplate, proceedingJoinPoint, returnObject);
            activityProviderAdaptor.send(activity, postActivity, level);
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
            log.error("Error when proceeding joint point: ", t);
            log.error("Possibly an expected error, since error logging activity is fired");
            try {
                String activityTemplate = errorActivity.value();
                Level level = errorActivity.level();
                log.debug("activityTemplate: {}", activityTemplate);
                ParsedActivity<?> activity = activityParser.parseActivity(activityTemplate, proceedingJoinPoint);
                activityProviderAdaptor.send(activity, errorActivity, level);
            } catch (Exception e) {
                log.error("Exception in errorActivity: ", e);
            }
            throw t;
        }
    }


}
