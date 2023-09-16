package com.owlcitydev.activitypoc.tester;

import com.owlcitydev.activitypoc.activity.configuration.ActivityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

@Configuration
public class TesterActivityConfiguration {
    @Bean
    public ActivityConfiguration activityConfiguration() {
        return ActivityConfiguration.builder()
                .registeredActivityProviders(List.of(new TestSlf4jActivityProvider()))
                .defaultActivityParamsClass(TesterActivityParams.class)
                .expressionParser(new SpelExpressionParser())
                .build();
    }

}
