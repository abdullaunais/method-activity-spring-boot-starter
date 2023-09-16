package io.github.abdullaunais.methodactivity.autoconfigure;

import io.github.abdullaunais.methodactivity.core.configuration.ActivityConfiguration;
import io.github.abdullaunais.methodactivity.core.event.IActivityEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.github.abdullaunais.methodactivity.**")
@ConditionalOnMissingBean(ActivityAutoConfiguration.class)
@ConditionalOnBean(annotation = EnableMethodActivity.class)
public class ActivityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(ActivityConfiguration.class)
    public ActivityConfiguration activityConfiguration() {
        return ActivityConfiguration.configure().build();
    }

    @Bean
    @ConditionalOnMissingBean(IActivityEvent.class)
    public IActivityEvent activityEvent() {
        return new DefaultSlf4jLoggingActivityListener();
    }
}
