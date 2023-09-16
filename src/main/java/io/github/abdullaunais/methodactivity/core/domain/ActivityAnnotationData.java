package io.github.abdullaunais.methodactivity.core.domain;

import io.github.abdullaunais.methodactivity.core.annotations.param.BaseActivityParams;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ActivityAnnotationData {
    @Builder.Default
    private String template = "''";
    @Builder.Default
    private String entity = "generic";
    @Builder.Default
    private String entityId = "''";
    @Builder.Default
    private Class<?> paramClass = BaseActivityParams.class;
    @Builder.Default
    private ActivityLevel level = ActivityLevel.ERROR;
}
