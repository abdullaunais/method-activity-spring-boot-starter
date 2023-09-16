package com.owlcitydev.activitypoc.tester;

import com.owlcitydev.activitypoc.activity.annotations.param.ParamExpression;
import lombok.Data;

@Data
public class TesterActivityParams {
    @ParamExpression("#authentication.principal.username")
    private String tester_name;
    @ParamExpression("@systemProperties['java.version']")
    private String java_version;
}
