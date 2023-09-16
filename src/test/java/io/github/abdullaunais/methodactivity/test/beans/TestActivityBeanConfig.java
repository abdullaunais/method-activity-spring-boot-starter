package io.github.abdullaunais.methodactivity.test.beans;

import io.github.abdullaunais.methodactivity.core.configuration.ActivityConfiguration;
import io.github.abdullaunais.methodactivity.core.configuration.DefaultVariables;
import io.github.abdullaunais.methodactivity.core.domain.ActivityLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Stack;

@Configuration
public class TestActivityBeanConfig {
    @Bean
    public ActivityConfiguration activityConfiguration(Stack<String> messageStack) {
        return ActivityConfiguration.configure()
                .withSecurityContextEnabled(true)
                .withActivityLevel(ActivityLevel.DEBUG)
                .withDefaultVariables(DefaultVariables.configure()
                        .executionTimeVariable("executionTime")
                        .build())
                .build();
    }

    @Bean
    public Stack<String> messageStack() {
        return new Stack<>();
    }

}
