package io.github.abdullaunais.methodactivity.core.domain;

import io.github.abdullaunais.methodactivity.core.annotations.param.BaseActivityParams;
import lombok.Data;

@Data
public class ParsedActivity<T extends BaseActivityParams> {
    private String activity;
    private String entity;
    private String entityId;
    private ActivityLevel activityLevel;
    private T params;
}
