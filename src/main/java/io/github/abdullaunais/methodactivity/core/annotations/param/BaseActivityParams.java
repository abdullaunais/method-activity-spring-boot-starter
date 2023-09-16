package io.github.abdullaunais.methodactivity.core.annotations.param;

import lombok.Data;

@Data
public class BaseActivityParams {
    // common params
    @ParamExpression("${spring.method-activity.default-variables.simple-class-name-variable:#simpleClassName}")
    private String simpleClassName;
    @ParamExpression("${spring.method-activity.default-variables.full-class-name-variable:#fullClassName}")
    private String fullClassName;
    @ParamExpression("${spring.method-activity.default-variables.method-name-variable:#methodName}")
    private String methodName;
    @ParamExpression("${spring.method-activity.default-variables.package-name-variable:#packageName}")
    private String packageName;

    // post params
    @ParamExpression("${spring.method-activity.default-variables.execution-time-variable:#executionTime}")
    private Long executionTime;
    @ParamExpression("${spring.method-activity.default-variables.return-object-variable:#returnObject}")
    private Object returnObject;

    // error params
    @ParamExpression("${spring.method-activity.default-variables.exception-variable:#exception}")
    private Exception exception;
}
