package com.owlcitydev.activitypoc.tester;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class TesterApplicationBootstrap implements ApplicationListener<ApplicationStartedEvent> {

    private final ApplicationContext applicationContext;
    private final TesterService testerService;

    public TesterApplicationBootstrap(ApplicationContext applicationContext, TesterService testerService) {
        this.applicationContext = applicationContext;
        this.testerService = testerService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Application bootstrap completed");
        this.logBeanNames();
        this.setSecurityContext();
        log.info("--------------------");
        testerService.init();
        log.info("--------------------");

        testerService.testPreActivity();
        log.info("--------------------");
        testerService.testPreActivityWithArgStrings("Test testPreActivityWithArgStrings", "Works!");
        log.info("--------------------");
        testerService.testPreActivityWithArgObjects("Test testPreActivityWithArgObjects with", new User("hello_arg_user", "test_password", new HashSet<>()));
        log.info("--------------------");
        testerService.testPreActivityWithCustomParamClass();
        log.info("--------------------");
        testerService.testPreActivityWithEnvironmentProperty();
        log.info("--------------------");
        testerService.testPreActivityWithSystemProperty();
        log.info("--------------------");
        testerService.testPreActivityWithAuthenticationPrincipalProperty();
        log.info("--------------------");


        testerService.testPostActivityWithReturnObject();
        log.info("--------------------");

        testerService.testPrePostActivityWithReturnObject();
        log.info("--------------------");

        try {
            testerService.testPrePostErrorActivityWithReturnObject();
        } catch (Exception e) {
            // ignore
        }
    }

    private void logBeanNames() {
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        if (autowireCapableBeanFactory instanceof SingletonBeanRegistry) {
            String[] singletonNames = ((SingletonBeanRegistry) autowireCapableBeanFactory).getSingletonNames();
            for (String singleton : singletonNames) {
                log.trace(singleton);
            }
        }
    }

    private void setSecurityContext() {
        log.info("Setting security context");
        User principal = new User("test_user", "test_password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        Map<String, String> credentials = Map.of(
                "name", "Test User",
                "email", "test@test.com"
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, credentials, new HashSet<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
