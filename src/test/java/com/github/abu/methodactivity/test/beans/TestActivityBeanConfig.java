package com.github.abu.methodactivity.test.beans;

import com.github.abu.methodactivity.activity.configuration.ActivityConfiguration;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.test.providers.TestMessageStackActivityProvider;
import com.github.abu.methodactivity.test.providers.TestSlf4jActivityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Stack;

@Configuration
public class TestActivityBeanConfig {
    @Bean
    public ActivityConfiguration activityConfiguration(Stack<String> messageStack) {
        return ActivityConfiguration.configure()
                .withActivityLevel(ActivityLevel.DEBUG)
                .withRegisteredActivityProviders(List.of(new TestSlf4jActivityProvider(), new TestMessageStackActivityProvider(messageStack)))
                .withVariableNames(ActivityConfiguration.DefaultVariables.configure()
                        .executionTimeVariableName("executionTime")
                        .build())
                .build();
    }

    @Bean
    public Stack<String> messageStack() {
        return new Stack<>();
    }

}
