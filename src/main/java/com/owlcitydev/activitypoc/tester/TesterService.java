package com.owlcitydev.activitypoc.tester;

import com.owlcitydev.activitypoc.activity.annotations.activity.ErrorActivity;
import com.owlcitydev.activitypoc.activity.annotations.activity.PostActivity;
import com.owlcitydev.activitypoc.activity.annotations.activity.PreActivity;
import com.owlcitydev.activitypoc.activity.domain.ActivityLevel;
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


    @PreActivity("new String('Test testPreActivity works!')")
    public void testPreActivity() {
        log.debug("TesterService.testPreActivity() invoked");
    }

    @PreActivity("#arg1 + ' ' + #arg2")
    public void testPreActivityWithArgs(String arg1, String arg2) {
        log.debug("TesterService.testPreActivity() invoked with args: {}, {}", arg1, arg2);
    }

    @PreActivity("'Test environment prop works with tester.prop=' + @environment.getProperty('tester.prop')")
    public void testPreActivityWithEnvironmentProperty() {
        log.debug("TesterService.testPreActivityWithEnvironmentProperty() invoked");
    }

    @PreActivity(value = "'Test system prop works with ' + @systemProperties['java.home']", level = ActivityLevel.DEBUG)
    public void testPreActivityWithSystemProperty() {
        log.debug("TesterService.testPreActivityWithSystemProperty() invoked");
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

    @PreActivity("'Test pre activity with return object'")
    @PostActivity("#returnObject")
    public String testPrePostActivityWithReturnObject() {
        log.debug("TesterService.testPrePostActivityWithReturnObject() invoked");
        return "I am number two!";
    }

    @PreActivity("'Test pre activity with return object and error'")
    @ErrorActivity("'Test error activity with return object and error'")
    @PostActivity("#returnObject")
    public String testPrePostErrorActivityWithReturnObject() {
        log.debug("TesterService.testPrePostErrorActivityWithReturnObject() invoked");
        if (true)
            throw new RuntimeException("I am number three!");
        return "I am number three!";
    }

}
