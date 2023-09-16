package com.owlcitydev.activitypoc.activity.aspect;

import com.owlcitydev.activitypoc.activity.annotations.activity.ErrorActivity;
import com.owlcitydev.activitypoc.activity.annotations.activity.PostActivity;
import com.owlcitydev.activitypoc.activity.annotations.activity.PreActivity;
import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import com.owlcitydev.activitypoc.activity.parser.IActivityParser;
import com.owlcitydev.activitypoc.activity.provider.ActivityProviderAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
            log.debug("activityTemplate: {}", activityTemplate);
            ActivityLevel activityLevel = preActivity.level();
            log.debug("activityLevel: {}", activityLevel);
            Class<?> paramClass = preActivity.paramClass();
            log.debug("paramClass: {}", paramClass.getSimpleName());
            ParsedActivity<?> activity = activityParser.parseActivity(activityTemplate, paramClass, proceedingJoinPoint);
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
            log.debug("activityTemplate: {}", activityTemplate);
            ActivityLevel activityLevel = postActivity.level();
            log.debug("activityLevel: {}", activityLevel);
            Class<?> paramClass = postActivity.paramClass();
            log.debug("paramClass: {}", paramClass.getSimpleName());
            ParsedActivity<?> activity = activityParser.parseActivity(activityTemplate, paramClass, proceedingJoinPoint, returnObject);
            activityProviderAdaptor.send(activity, postActivity, activityLevel);
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
                String activityTemplate = errorActivity.value();
                log.debug("activityTemplate: {}", activityTemplate);
                ActivityLevel activityLevel = errorActivity.level();
                log.debug("activityLevel: {}", activityLevel);
                Class<?> paramClass = errorActivity.paramClass();
                log.debug("paramClass: {}", paramClass.getSimpleName());
                ParsedActivity<?> activity = activityParser.parseActivity(activityTemplate, paramClass, proceedingJoinPoint);
                activityProviderAdaptor.send(activity, errorActivity, activityLevel);
            } catch (Exception e) {
                log.error("Exception in errorActivity: ", e);
            }
            throw t;
        }
    }


}
