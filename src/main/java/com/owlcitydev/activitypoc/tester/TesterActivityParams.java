package com.owlcitydev.activitypoc.tester;

import com.owlcitydev.activitypoc.activity.annotations.param.ParamExpression;
import com.owlcitydev.activitypoc.activity.domain.BaseActivityParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TesterActivityParams extends BaseActivityParams {
    @ParamExpression("#authentication.principal.username")
    private String tester_name;
    @ParamExpression("@systemProperties['java.version']")
    private String java_version;
}
