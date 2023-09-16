package com.github.abu.methodactivity.test.paramclasses;

import com.github.abu.methodactivity.activity.annotations.param.ParamExpression;
import com.github.abu.methodactivity.activity.domain.BaseActivityParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestActivityParams extends BaseActivityParams {
    @ParamExpression("#authentication.principal.username")
    private String test_user;
    @ParamExpression("@systemProperties['java.version']")
    private String java_version;
    @ParamExpression("${test.parse-at-param-class}")
    private String test_spring_config;
    @ParamExpression("#executionTime")
    private Long executionTime;
}
