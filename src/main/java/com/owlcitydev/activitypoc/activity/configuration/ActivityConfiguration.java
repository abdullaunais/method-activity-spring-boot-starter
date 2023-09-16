package com.owlcitydev.activitypoc.activity.configuration;

import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;
import com.owlcitydev.activitypoc.activity.provider.IActivityProvider;
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
