package com.github.abu.methodactivity.activity.configuration;

import com.github.abu.methodactivity.activity.provider.IActivityProvider;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.expression.ExpressionParser;

import java.util.List;

@Data
@Builder
public class ActivityConfiguration {
    private ExpressionParser expressionParser;
    private List<IActivityProvider> registeredActivityProviders;
    private ActivityLevel activityLevel;
}
