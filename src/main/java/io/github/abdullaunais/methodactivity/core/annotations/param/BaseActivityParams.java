package io.github.abdullaunais.methodactivity.core.annotations.param;

import lombok.Data;

@Data
public class BaseActivityParams {
    @ParamExpression("${spring.method-activity.default-variables.simple-class-name-variable:#simpleClassName}")
    private String simpleClassName;
    @ParamExpression("${spring.method-activity.default-variables.full-class-name-variable:#fullClassName}")
    private String fullClassName;
    @ParamExpression("${spring.method-activity.default-variables.method-name-variable:#methodName}")
    private String methodName;
    @ParamExpression("${spring.method-activity.default-variables.package-name-variable:#packageName}")
    private String packageName;
}
