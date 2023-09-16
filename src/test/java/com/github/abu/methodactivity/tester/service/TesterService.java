package com.github.abu.methodactivity.tester.service;

import com.github.abu.methodactivity.activity.annotations.activity.ErrorActivity;
import com.github.abu.methodactivity.activity.annotations.activity.PostActivity;
import com.github.abu.methodactivity.activity.annotations.activity.PreActivity;
import com.github.abu.methodactivity.activity.annotations.param.ExpressionAlias;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.tester.paramclasses.TesterActivityParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;


@Slf4j
@Service
public class TesterService {

    @Value("${tester.throw-exception}")
    private boolean throwException;

    @PreActivity(value = "new String('Test testPreActivity works!')", paramClass = TesterActivityParams.class)
    public void invokePreActivity() {
        log.debug("TesterService.invokePreActivity() invoked");
    }

    @PreActivity("#arg1 + ' ' + #arg2")
    public void invokePreActivityWithArgStrings(String arg1, String arg2) {
        log.debug("TesterService.invokePreActivityWithArgStrings() invoked with args: {}, {}", arg1, arg2);
    }

    @PreActivity(value = "#arg1 + ' ' + #user?.username", entity = "user", entityId = "#user?.username")
    public void invokePreActivityWithArgObjects(String arg1, @ExpressionAlias("user") User user2) {
        log.debug("TesterService.invokePreActivityWithArgObjects() invoked");
    }

    @PreActivity(value = "'Test environment prop works with tester.prop=' + @environment.getProperty('tester.prop')", paramClass = TesterActivityParams.class)
    public void invokePreActivityWithCustomParamClass() {
        log.debug("TesterService.invokePreActivityWithCustomParamClass() invoked");
    }

    @PreActivity("'Test environment prop works with tester.prop=' + @environment.getProperty('tester.prop')")
    public void invokePreActivityWithEnvironmentProperty() {
        log.debug("TesterService.invokePreActivityWithEnvironmentProperty() invoked");
    }

    @PreActivity(value = "'Test system prop works with ' + @systemProperties['java.home']", level = ActivityLevel.DEBUG)
    public void invokePreActivityWithSystemProperty() {
        log.debug("TesterService.invokePreActivityWithSystemProperty() invoked");
    }


    @PreActivity(value = "${tester.template}", level = ActivityLevel.DEBUG)
    public void invokePreActivityWithSpringProperty() {
        log.debug("TesterService.invokePreActivityWithSpringProperty() invoked");
    }


    @PreActivity(value = "'Test principal username with ' + #authentication?.principal?.username", level = ActivityLevel.ERROR)
    public void invokePreActivityWithAuthenticationPrincipalProperty() {
        log.debug("TesterService.invokePreActivityWithAuthenticationPrincipalProperty() invoked");
    }


    @PostActivity("#returnObject")
    public User invokePostActivityWithReturnObject() {
        log.debug("TesterService.invokePostActivityWithReturnObject() invoked");
        return new User("i_am_return_username", "return_pwd", new HashSet<>());
    }

    @PreActivity("'Test pre post activity with return object'")
    @PostActivity("#returnObject")
    public String invokePrePostActivityWithReturnObject() {
        log.debug("TesterService.invokePrePostActivityWithReturnObject() invoked");
        return "I am number two!";
    }

    @PreActivity("'Test pre post activity with return object and error'")
    @ErrorActivity("'Test error activity with return object and error'")
    @PostActivity("#returnObject")
    public String invokePrePostErrorActivityWithReturnObject() {
        log.debug("TesterService.invokePrePostErrorActivityWithReturnObject() invoked");
        if (throwException)
            throw new RuntimeException("I am a runtime exception message!");
        return "I am number three!";
    }

}
