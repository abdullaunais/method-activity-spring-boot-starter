package com.github.abu.methodactivity.tester.beans;

import com.github.abu.methodactivity.activity.configuration.ActivityConfiguration;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.tester.providers.TesterMessageStackActivityProvider;
import com.github.abu.methodactivity.tester.providers.TesterSlf4jActivityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Stack;

@Configuration
public class TesterActivityBeanConfig {
    @Bean
    public ActivityConfiguration activityConfiguration(Stack<String> messageStack) {
        return ActivityConfiguration.builder()
                .activityLevel(ActivityLevel.DEBUG)
                .registeredActivityProviders(List.of(new TesterSlf4jActivityProvider(), new TesterMessageStackActivityProvider(messageStack)))
                .expressionParser(new SpelExpressionParser())
                .build();
    }

    @Bean
    public Stack<String> messageStack() {
        return new Stack<>();
    }

}
