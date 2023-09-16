package io.github.abdullaunais.methodactivity.core.annotations.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ErrorActivityParams extends BaseActivityParams {
    @ParamExpression("${spring.method-activity.default-variables.exception-variable:#exception}")
    private Exception exception;
}
