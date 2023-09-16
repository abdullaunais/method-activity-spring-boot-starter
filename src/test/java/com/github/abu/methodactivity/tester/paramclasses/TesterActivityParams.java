package com.github.abu.methodactivity.tester.paramclasses;

import com.github.abu.methodactivity.activity.annotations.param.ParamExpression;
import com.github.abu.methodactivity.activity.domain.BaseActivityParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TesterActivityParams extends BaseActivityParams {
    @ParamExpression("#authentication.principal.username")
    private String tester_name;
    @ParamExpression("@systemProperties['java.version']")
    private String java_version;
    @ParamExpression("${tester.parse-at-param-class}")
    private String tester_spring_config;
}
