package io.github.abdullaunais.methodactivity.test.paramclasses;

import io.github.abdullaunais.methodactivity.core.annotations.param.BaseActivityParams;
import io.github.abdullaunais.methodactivity.core.annotations.param.ParamExpression;
import io.github.abdullaunais.methodactivity.core.annotations.param.PreActivityParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TestPreActivityParams extends PreActivityParams {
    @ParamExpression("#authentication.principal.username")
    private String test_user;
    @ParamExpression("@systemProperties['java.version']")
    private String java_version;
    @ParamExpression("${test.parse-at-param-class}")
    private String test_spring_config;
}
