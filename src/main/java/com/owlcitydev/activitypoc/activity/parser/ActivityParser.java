package com.owlcitydev.activitypoc.activity.parser;

import com.owlcitydev.activitypoc.activity.annotations.param.ParamExpression;
import com.owlcitydev.activitypoc.activity.configuration.ActivityConfiguration;
import com.owlcitydev.activitypoc.activity.domain.ParsedActivity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Component
public class ActivityParser implements IActivityParser {
    private final ActivityConfiguration activityConfiguration;
    private final ApplicationContext applicationContext;
    private final ExpressionParser expressionParser;

    public ActivityParser(ActivityConfiguration activityConfiguration, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.activityConfiguration = activityConfiguration;
        this.expressionParser = activityConfiguration.getExpressionParser();
    }

    @Override
    public <T> ParsedActivity<T> parseActivity(String activityTemplate, ProceedingJoinPoint proceedingJoinPoint, Object returnObject) {
        log.trace("pointcut argument size: {}", proceedingJoinPoint.getArgs().length);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(this.applicationContext));

        CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();
        IntStream.range(0, proceedingJoinPoint.getArgs().length).boxed()
                .forEach(index -> context.setVariable(codeSignature.getParameterNames()[index], proceedingJoinPoint.getArgs()[index]));

        context.setVariable("authentication", SecurityContextHolder.getContext().getAuthentication());
        Optional.ofNullable(returnObject).ifPresent(obj -> context.setVariable("returnObject", obj));

        Object activityObj = expressionParser.parseExpression(activityTemplate)
                .getValue(context, applicationContext, Object.class);

        try {
            T activityParams = ParsedActivity
                    .createInstance(activityConfiguration.getDefaultActivityParamsClass());
            Arrays.stream(activityConfiguration.getDefaultActivityParamsClass().getDeclaredFields())
                    .forEach(field -> {
                        ParamExpression paramExpression = field.getAnnotation(ParamExpression.class);
                        Optional.ofNullable(paramExpression)
                                .ifPresent(annotation -> {
                                    String expression = annotation.value();
                                    Object value = expressionParser.parseExpression(expression).getValue(context);
                                    try {
                                        field.setAccessible(true);
                                        field.set(activityParams, value);
                                    } catch (Exception e) {
                                        log.warn("error in setting param field value: {}", field.getName());
                                    }
                                });
                    });
            ParsedActivity<T> parsedActivity = new ParsedActivity<>();
            parsedActivity.setActivity(Optional.ofNullable(activityObj).orElse("").toString());
            parsedActivity.setParams(activityParams);
            return parsedActivity;
        } catch (Exception e) {
            log.error("Exception in creating activity params instance: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> ParsedActivity<T> parseActivity(String activityTemplate, ProceedingJoinPoint proceedingJoinPoint) {
        return this.parseActivity(activityTemplate, proceedingJoinPoint, null);
    }


}
