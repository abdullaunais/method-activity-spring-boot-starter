package com.github.abu.methodactivity.tester;

import com.github.abu.methodactivity.activity.configuration.ActivityConfiguration;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

@Configuration
public class TesterActivityConfiguration {
    @Bean
    public ActivityConfiguration activityConfiguration() {
        return ActivityConfiguration.builder()
                .activityLevel(ActivityLevel.DEBUG)
                .registeredActivityProviders(List.of(new TesterSlf4jActivityProvider()))
                .expressionParser(new SpelExpressionParser())
                .build();
    }

}
