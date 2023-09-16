package com.github.abu.methodactivity.activity.parser;

import com.github.abu.methodactivity.activity.annotations.param.ExpressionAlias;
import com.github.abu.methodactivity.activity.annotations.param.ParamExpression;
import com.github.abu.methodactivity.activity.configuration.ActivityConfiguration;
import com.github.abu.methodactivity.activity.domain.ActivityAnnotationData;
import com.github.abu.methodactivity.activity.domain.ParsedActivity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Component
public class ActivityParser implements IActivityParser {
    private final ActivityConfiguration activityConfiguration;
    private final ApplicationContext applicationContext;
    private final ExpressionParser expressionParser;
    private final Environment environment;

    public ActivityParser(ActivityConfiguration activityConfiguration, ApplicationContext applicationContext, Environment environment) {
        this.activityConfiguration = activityConfiguration;
        this.applicationContext = applicationContext;
        this.expressionParser = activityConfiguration.getExpressionParser();
        this.environment = environment;
    }

    @Override
    public <T> ParsedActivity<T> parseActivity(ActivityAnnotationData annotationData, ProceedingJoinPoint proceedingJoinPoint, Object returnObject, Long executionTime) {
        log.trace("pointcut argument size: {}", proceedingJoinPoint.getArgs().length);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(this.applicationContext));


        CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();

        IntStream.range(0, proceedingJoinPoint.getArgs().length)
                .forEach(index -> {
                    Optional<ExpressionAlias> expressionAlias = Optional.empty();
                    try {
                        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
                        String methodName = signature.getMethod().getName();
                        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
                        Annotation[] annotations = proceedingJoinPoint.getTarget().getClass()
                                .getMethod(methodName, parameterTypes).getParameterAnnotations()[index];
                        log.trace("annotations: {}", Arrays.stream(annotations).toArray());
                        expressionAlias = Arrays.stream(annotations)
                                .filter(a -> a instanceof ExpressionAlias)
                                .map(a -> (ExpressionAlias) a)
                                .findFirst();
                    } catch (Exception e) {
                        log.trace("Exception in reading parameter annotations: ", e);
                    }

                    String argName = expressionAlias
                            .map(ExpressionAlias::value)
                            .orElse(codeSignature.getParameterNames()[index]);
                    context.setVariable(argName, proceedingJoinPoint.getArgs()[index]);
                });

        ActivityConfiguration.DefaultVariables variableNames = activityConfiguration.getVariableNames();
        context.setVariable(variableNames.getAuthenticationVariableName(), SecurityContextHolder.getContext().getAuthentication());
        Optional.ofNullable(returnObject)
                .ifPresent(obj -> context.setVariable(variableNames.getReturnObjectVariableName(), obj));
        Optional.ofNullable(executionTime)
                .ifPresent(time -> context.setVariable(variableNames.getExecutionTimeVariableName(), time));

        String templateStr = annotationData.getTemplate();
        if (templateStr.startsWith("$")) {
            templateStr = environment.resolvePlaceholders(templateStr);
        }
        Object activityObj = expressionParser.parseExpression(templateStr)
                .getValue(context, applicationContext, Object.class);
        String entityId = expressionParser.parseExpression(annotationData.getEntityId())
                .getValue(context, applicationContext, String.class);

        try {
            T activityParams = ParsedActivity
                    .createInstance(annotationData.getParamClass());
            Arrays.stream(annotationData.getParamClass().getDeclaredFields())
                    .forEach(field -> {
                        ParamExpression paramExpression = field.getAnnotation(ParamExpression.class);
                        Optional.ofNullable(paramExpression)
                                .ifPresent(annotation -> {
                                    String expression = annotation.value();
                                    if (expression.startsWith("$")) {
                                        expression = environment.resolvePlaceholders(expression);
                                    }
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
            parsedActivity.setEntityId(entityId);
            parsedActivity.setEntity(annotationData.getEntity());
            return parsedActivity;
        } catch (Exception e) {
            log.error("Exception in creating activity params instance: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> ParsedActivity<T> parseActivity(ActivityAnnotationData annotationData, ProceedingJoinPoint proceedingJoinPoint) {
        return this.parseActivity(annotationData, proceedingJoinPoint, null, null);
    }


}
