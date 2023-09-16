package io.github.abdullaunais.methodactivity.core.annotations.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostActivityParams extends BaseActivityParams {
    @ParamExpression("${spring.method-activity.default-variables.execution-time-variable:#executionTime}")
    private Long executionTime;

    @ParamExpression("${spring.method-activity.default-variables.return-object-variable:#returnObject}")
    private Object returnObject;
}
