package com.github.abu.methodactivity;

import com.github.abu.methodactivity.tester.service.TesterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

/**
 * : (PreActivity) [generic:] [BaseActivityParams()]: Test system prop works with C:\Program Files\Java\jdk-20
 * : TesterService.invokePreActivityWithSystemProperty() invoked
 * : Setting security context
 * : Setting security context
 * : Setting security context
 * : (PreActivity) [generic:] [TesterActivityParams(tester_name=test_user, java_version=20.0.1, tester_spring_config=I am test_user, and I am parsed at param expression level)]: Test environment prop works with tester.prop=Sample Prop
 * : TesterService.invokePreActivityWithCustomParamClass() invoked
 * : Setting security context
 * : (PreActivity) [generic:] [BaseActivityParams()]: My name is test_user, and I am a template coming from the spring config file
 * : TesterService.invokePreActivityWithSpringProperty() invoked
 * : Setting security context
 * : TesterService.invokePostActivityWithReturnObject() invoked
 * : (PostActivity) [generic:] [BaseActivityParams()]: org.springframework.security.core.userdetails.User [Username=i_am_return_username, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[]]
 * : Setting security context
 * : (PreActivity) [generic:] [TesterActivityParams(tester_name=test_user, java_version=20.0.1, tester_spring_config=I am test_user, and I am parsed at param expression level)]: Test testPreActivity works!
 * : TesterService.invokePreActivity() invoked
 * : Setting security context
 * : (PreActivity) [generic:] [BaseActivityParams()]: Test environment prop works with tester.prop=Sample Prop
 * : TesterService.invokePreActivityWithEnvironmentProperty() invoked
 * : Setting security context
 * : (PreActivity) [generic:] [BaseActivityParams()]: Test pre post activity with return object
 * : TesterService.invokePrePostActivityWithReturnObject() invoked
 * : (PostActivity) [generic:] [BaseActivityParams()]: I am number two!
 * : Setting security context
 * : (PreActivity) [generic:] [BaseActivityParams()]: Test testPreActivityWithArgStrings Works!
 * : TesterService.invokePreActivityWithArgStrings() invoked with args: Test testPreActivityWithArgStrings, Works!
 * : Setting security context
 * : (PreActivity) [user:hello_arg_user] [BaseActivityParams()]: Test testPreActivityWithArgObjects with hello_arg_user
 * : TesterService.invokePreActivityWithArgObjects() invoked
 * : Setting security context
 * : Setting security context
 * : (PreActivity) [generic:] [BaseActivityParams()]: Test pre post activity with return object and error
 * : TesterService.invokePrePostErrorActivityWithReturnObject() invoked
 * : Error when proceeding joint point: I am a runtime exception message!
 * : Possibly an expected error, since error logging activity is fired
 * : (ErrorActivity) [generic:] [BaseActivityParams()]: Test error activity with return object and error
 * : Setting security context
 * : (PreActivity) [generic:] [BaseActivityParams()]: Test principal username with test_user
 * : TesterService.invokePreActivityWithAuthenticationPrincipalProperty() invoked
 */

@Slf4j
@SpringBootTest
@EnableAutoConfiguration
class ActivityPocApplicationTests {

    @Autowired
    private TesterService testerService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Stack<String> messageStack;

    @Test
    @BeforeEach
    void testInitialize() {
        Assert.notNull(messageStack, "messageStack not initialized");
        Assert.notNull(testerService, "testerService not initialized");
        Assert.notNull(applicationContext, "applicationContext not initialized");

        this.setSecurityContext();
        messageStack.clear();
    }


    @Test
    void testPreActivity() {
        testerService.invokePreActivity();
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test testPreActivity works!");
    }

    @Test
    void testPreActivityWithArgStrings() {
        testerService.invokePreActivityWithArgStrings("Test testPreActivityWithArgStrings", "Works!");
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test testPreActivityWithArgStrings Works!");
    }

    @Test
    void testPreActivityWithArgObjects() {
        testerService.invokePreActivityWithArgObjects("Test testPreActivityWithArgObjects with", new User("hello_arg_user", "test_password", new HashSet<>()));
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test testPreActivityWithArgObjects with hello_arg_user");
    }

    @Test
    void testPreActivityWithCustomParamClass() {
        testerService.invokePreActivityWithCustomParamClass();
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test environment prop works with tester.prop=Sample Prop");
    }

    @Test
    void testPreActivityWithEnvironmentProperty() {
        testerService.invokePreActivityWithEnvironmentProperty();
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test environment prop works with tester.prop=Sample Prop");
    }

    @Test
    void testPreActivityWithSystemProperty() {
        testerService.invokePreActivityWithSystemProperty();
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test system prop works with C:\\Program Files\\Java\\jdk-20");
    }

    @Test
    void testPreActivityWithSpringProperty() {
        testerService.invokePreActivityWithSpringProperty();
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "My name is test_user, and I am a template coming from the spring config file");
    }

    @Test
    void testPreActivityWithAuthenticationPrincipalProperty() {
        testerService.invokePreActivityWithAuthenticationPrincipalProperty();
        assertEquals(messageStack.size(), 1);
        assertEquals(messageStack.pop(), "Test principal username with test_user");
    }

    @Test
    void testPostActivityWithReturnObject() {
        User testReturn = testerService.invokePostActivityWithReturnObject();
        assertEquals(testReturn.getUsername(), "i_am_return_username");
        assertEquals(messageStack.size(), 1);
        assertTrue(messageStack.pop().contains("org.springframework.security.core.userdetails.User"));
    }

    @Test
    void testPrePostActivityWithReturnObject() {
        String testReturn = testerService.invokePrePostActivityWithReturnObject();
        assertEquals(testReturn, "I am number two!");
        assertEquals(messageStack.size(), 2);
        assertEquals(messageStack.pop(), "I am number two!");
        assertEquals(messageStack.pop(), "Test pre post activity with return object");
    }

    @Test
    void testPrePostErrorActivityWithReturnObject() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            String testReturn = testerService.invokePrePostErrorActivityWithReturnObject();
            log.info("testReturn: {}", testReturn);
        });
        assertEquals(messageStack.size(), 2);
        assertEquals(exception.getMessage(), "I am a runtime exception message!");
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
