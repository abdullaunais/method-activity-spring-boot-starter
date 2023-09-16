package io.github.abdullaunais.methodactivity.core.configuration;

import io.github.abdullaunais.methodactivity.autoconfigure.EnableMethodActivity;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@Builder(builderMethodName = "configure")
@ConditionalOnBean(annotation = EnableMethodActivity.class)
@EnableConfigurationProperties(DefaultVariables.class)
@ConfigurationProperties(prefix = "spring.method-activity.default-variables")
public class DefaultVariables {
    @Builder.Default
    private String authenticationVariable = "authentication";
    @Builder.Default
    private String simpleClassNameVariable = "simpleClassName";
    @Builder.Default
    private String fullClassNameVariable = "fullClassName";
    @Builder.Default
    private String methodNameVariable = "methodName";
    @Builder.Default
    private String packageNameVariable = "packageName";
    @Builder.Default
    private String returnObjectVariable = "returnObject";
    @Builder.Default
    private String executionTimeVariable = "executionTime";
    @Builder.Default
    private String exceptionVariable = "exception";
}
