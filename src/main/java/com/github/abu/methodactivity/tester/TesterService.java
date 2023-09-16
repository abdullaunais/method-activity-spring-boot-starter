package com.github.abu.methodactivity.tester;

import com.github.abu.methodactivity.activity.annotations.activity.ErrorActivity;
import com.github.abu.methodactivity.activity.annotations.activity.PostActivity;
import com.github.abu.methodactivity.activity.annotations.activity.PreActivity;
import com.github.abu.methodactivity.activity.annotations.param.ExpressionAlias;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;


@Slf4j
@Service
public class TesterService {

    public void init() {
        log.debug("TesterService.init() invoked");
    }


    @PreActivity(value = "new String('Test testPreActivity works!')", paramClass = TesterActivityParams.class)
    public void testPreActivity() {
        log.debug("TesterService.testPreActivity() invoked");
    }

    @PreActivity("#arg1 + ' ' + #arg2")
    public void testPreActivityWithArgStrings(String arg1, String arg2) {
        log.debug("TesterService.testPreActivityWithArgStrings() invoked with args: {}, {}", arg1, arg2);
    }

    @PreActivity(value = "#arg1 + ' ' + #user?.username", entity = "user", entityId = "#user?.username")
    public void testPreActivityWithArgObjects(String arg1, @ExpressionAlias("user") User user2) {
        log.debug("TesterService.testPreActivityWithArgObjects() invoked");
    }

    @PreActivity(value = "'Test environment prop works with tester.prop=' + @environment.getProperty('tester.prop')", paramClass = TesterActivityParams.class)
    public void testPreActivityWithCustomParamClass() {
        log.debug("TesterService.testPreActivityWithCustomParamClass() invoked");
    }

    @PreActivity("'Test environment prop works with tester.prop=' + @environment.getProperty('tester.prop')")
    public void testPreActivityWithEnvironmentProperty() {
        log.debug("TesterService.testPreActivityWithEnvironmentProperty() invoked");
    }

    @PreActivity(value = "'Test system prop works with ' + @systemProperties['java.home']", level = ActivityLevel.DEBUG)
    public void testPreActivityWithSystemProperty() {
        log.debug("TesterService.testPreActivityWithSystemProperty() invoked");
    }


    @PreActivity(value = "${tester.template}", level = ActivityLevel.DEBUG)
    public void testPreActivityWithSpringProperty() {
        log.debug("TesterService.testPreActivityWithSpringProperty() invoked");
    }


    @PreActivity(value = "'Test principal username with ' + #authentication?.principal?.username", level = ActivityLevel.ERROR)
    public void testPreActivityWithAuthenticationPrincipalProperty() {
        log.debug("TesterService.testPreActivityWithAuthenticationPrincipalProperty() invoked");
    }


    @PostActivity("#returnObject")
    public User testPostActivityWithReturnObject() {
        log.debug("TesterService.testPostActivityWithReturnObject() invoked");
        return new User("i_am_return_username", "return_pwd", new HashSet<>());
    }

    @PreActivity("'Test pre post activity with return object'")
    @PostActivity("#returnObject")
    public String testPrePostActivityWithReturnObject() {
        log.debug("TesterService.testPrePostActivityWithReturnObject() invoked");
        return "I am number two!";
    }

    @PreActivity("'Test pre post activity with return object and error'")
    @ErrorActivity("'Test error activity with return object and error'")
    @PostActivity("#returnObject")
    public String testPrePostErrorActivityWithReturnObject() {
        log.debug("TesterService.testPrePostErrorActivityWithReturnObject() invoked");
        if (true)
            throw new RuntimeException("I am a runtime exception message!");
        return "I am number three!";
    }

}
