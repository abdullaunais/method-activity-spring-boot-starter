package com.github.abu.methodactivity.activity.configuration;

import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.activity.provider.IActivityProvider;
import lombok.Builder;
import lombok.Data;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

@Data
@Builder(setterPrefix = "with", builderMethodName = "configure")
public class ActivityConfiguration {
    @Builder.Default
    private ExpressionParser expressionParser = new SpelExpressionParser();
    @Builder.Default
    private ActivityLevel activityLevel = ActivityLevel.INFO;
    @Builder.Default
    private DefaultVariables variableNames = DefaultVariables.configure().build();

    private List<IActivityProvider> registeredActivityProviders;

    @Data
    @Builder(builderMethodName = "configure")
    public static class DefaultVariables {
        @Builder.Default
        private String authenticationVariableName = "authentication";
        @Builder.Default
        private String returnObjectVariableName = "returnObject";
        @Builder.Default
        private String executionTimeVariableName = "executionTime";
    }
}
