package io.github.abdullaunais.methodactivity.core.configuration;

import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;
import io.github.abdullaunais.methodactivity.autoconfigure.DefaultSlf4jLoggingActivityListener;
import io.github.abdullaunais.methodactivity.core.event.IActivityEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

@Data
@Builder(setterPrefix = "with", builderMethodName = "configure")
@ConfigurationProperties(prefix = "spring.method-activity")
public class ActivityConfiguration {
    @Builder.Default
    private ActivityLevel activityLevel = ActivityLevel.INFO;
    @Builder.Default
    private boolean securityContextEnabled = false;
    @Builder.Default
    private DefaultVariables defaultVariables = DefaultVariables.configure().build();
}
