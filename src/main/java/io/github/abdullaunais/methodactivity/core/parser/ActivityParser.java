package io.github.abdullaunais.methodactivity.core.parser;

import io.github.abdullaunais.methodactivity.autoconfigure.EnableMethodActivity;
import io.github.abdullaunais.methodactivity.core.annotations.param.*;
import io.github.abdullaunais.methodactivity.core.configuration.ActivityConfiguration;
import io.github.abdullaunais.methodactivity.core.configuration.DefaultVariables;
import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Component
@ConditionalOnBean(annotation = EnableMethodActivity.class)
public class ActivityParser implements IActivityParser {
    private final ActivityConfiguration activityConfiguration;
    private final ApplicationContext applicationContext;
    private final ExpressionParser expressionParser;
    private final Environment environment;

    public ActivityParser(ActivityConfiguration activityConfiguration, ApplicationContext applicationContext, Environment environment) {
        this.activityConfiguration = activityConfiguration;
        this.applicationContext = applicationContext;
        this.expressionParser = new SpelExpressionParser();
        this.environment = environment;
    }

    @Override
    public <T extends BaseActivityParams> ParsedActivity<T> parseActivity(ActivityType activityType, ActivityAnnotationData annotationData, ProceedingJoinPoint proceedingJoinPoint, T activityParams) {
        log.trace("pointcut argument size: {}", proceedingJoinPoint.getArgs().length);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(this.applicationContext));

        CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String fullClassName = signature.getDeclaringType().getName();
        String simpleClassName = signature.getDeclaringType().getSimpleName();
        String packageName = signature.getDeclaringType().getPackageName();

        IntStream.range(0, proceedingJoinPoint.getArgs().length)
                .forEach(index -> {
                    Optional<ExpressionAlias> expressionAlias = Optional.empty();
                    try {
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

        DefaultVariables variableNames = activityConfiguration.getDefaultVariables();

        if (activityConfiguration.isSecurityContextEnabled()) {
            context.setVariable(variableNames.getAuthenticationVariable(), SecurityContextHolder.getContext().getAuthentication());
        }

        switch (activityType) {
            case PostActivity -> {
                Optional.ofNullable(activityParams.getReturnObject())
                        .ifPresent(obj -> context.setVariable(variableNames.getReturnObjectVariable(), obj));
                Optional.ofNullable(activityParams.getExecutionTime())
                        .ifPresent(time -> context.setVariable(variableNames.getExecutionTimeVariable(), time));
            }
            case ErrorActivity -> {
                Optional.ofNullable(activityParams.getException())
                        .ifPresent(ex -> context.setVariable(variableNames.getExceptionVariable(), ex));
            }
        }


        context.setVariable(variableNames.getMethodNameVariable(), methodName);
        context.setVariable(variableNames.getFullClassNameVariable(), fullClassName);
        context.setVariable(variableNames.getSimpleClassNameVariable(), simpleClassName);
        context.setVariable(variableNames.getPackageNameVariable(), packageName);


        String templateStr = annotationData.getTemplate();
        if (templateStr.startsWith("$")) {
            templateStr = environment.resolvePlaceholders(templateStr);
        }
        Object activityObj = expressionParser.parseExpression(templateStr)
                .getValue(context, applicationContext, Object.class);
        String entityId = expressionParser.parseExpression(annotationData.getEntityId())
                .getValue(context, applicationContext, String.class);

        try {
            List<Field> fieldStream = new ArrayList<>();
            Class<?> clazz = annotationData.getParamClass();
            while (clazz != null) {
                fieldStream.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass();
            }
            fieldStream.forEach(field -> {
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
            parsedActivity.setActivityLevel(annotationData.getLevel());
            return parsedActivity;
        } catch (Exception e) {
            log.error("Exception in creating activity params instance: ", e);
            throw new RuntimeException(e);
        }
    }
}
