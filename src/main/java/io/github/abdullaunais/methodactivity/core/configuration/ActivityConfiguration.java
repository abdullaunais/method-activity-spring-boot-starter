package io.github.abdullaunais.methodactivity.core.configuration;

import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
