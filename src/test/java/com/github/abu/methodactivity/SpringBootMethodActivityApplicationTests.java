package com.github.abu.methodactivity;

import com.github.abu.methodactivity.activity.annotations.configuration.EnableMethodActivity;
import com.github.abu.methodactivity.test.service.TestService;
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


@Slf4j
@SpringBootTest
@EnableMethodActivity
@EnableAutoConfiguration
class SpringBootMethodActivityApplicationTests {

    @Autowired
    private TestService testService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Stack<String> messageStack;

    @Test
    @BeforeEach
    void testInitialize() {
        Assert.notNull(messageStack, "messageStack not initialized");
        Assert.notNull(testService, "testService not initialized");
        Assert.notNull(applicationContext, "applicationContext not initialized");

        this.setSecurityContext();
        messageStack.clear();
    }
//
//
//    @Test
//    void testPreActivity() {
//        testService.invokePreActivity();
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test testPreActivity works!");
//    }
//
//    @Test
//    void testPreActivityWithArgStrings() {
//        testService.invokePreActivityWithArgStrings("Test testPreActivityWithArgStrings", "Works!");
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test testPreActivityWithArgStrings Works!");
//    }
//
//    @Test
//    void testPreActivityWithArgObjects() {
//        testService.invokePreActivityWithArgObjects("Test testPreActivityWithArgObjects with", new User("hello_arg_user", "test_password", new HashSet<>()));
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test testPreActivityWithArgObjects with hello_arg_user");
//    }
//
//    @Test
//    void testPreActivityWithCustomParamClass() {
//        testService.invokePreActivityWithCustomParamClass();
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test environment prop works with test.prop=Sample Prop");
//    }
//
//    @Test
//    void testPreActivityWithEnvironmentProperty() {
//        testService.invokePreActivityWithEnvironmentProperty();
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test environment prop works with test.prop=Sample Prop");
//    }
//
//    @Test
//    void testPreActivityWithSystemProperty() {
//        testService.invokePreActivityWithSystemProperty();
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test system prop works with C:\\Program Files\\Java\\jdk-20");
//    }
//
//    @Test
//    void testPreActivityWithSpringProperty() {
//        testService.invokePreActivityWithSpringProperty();
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "My name is test_user, and I am a template coming from the spring config file");
//    }
//
//    @Test
//    void testPreActivityWithAuthenticationPrincipalProperty() {
//        testService.invokePreActivityWithAuthenticationPrincipalProperty();
//        assertEquals(messageStack.size(), 1);
//        assertEquals(messageStack.pop(), "Test principal username with test_user");
//    }
//
//    @Test
//    void testPostActivityWithReturnObject() {
//        User testReturn = testService.invokePostActivityWithReturnObject();
//        assertEquals(testReturn.getUsername(), "i_am_return_username");
//        assertEquals(messageStack.size(), 1);
//        assertTrue(messageStack.pop().contains("org.springframework.security.core.userdetails.User"));
//    }
//
//    @Test
//    void testPostActivityExecutionTimeCalculation() {
//        testService.invokePostActivityExecutionTimeCalculation();
//        assertEquals(messageStack.size(), 1);
//        String message = messageStack.pop();
//        assertTrue(message.contains("I have executed in"));
//        assertTrue(message.contains(" ms"));
//        assertTrue(message.split(" ")[4].chars().allMatch(Character::isDigit));
//
//    }
//
//    @Test
//    void testPrePostActivityWithReturnObject() {
//        String testReturn = testService.invokePrePostActivityWithReturnObject();
//        assertEquals(testReturn, "I am number two!");
//        assertEquals(messageStack.size(), 2);
//        assertEquals(messageStack.pop(), "I am number two!");
//        assertEquals(messageStack.pop(), "Test pre post activity with return object");
//    }
//
//    @Test
//    void testPrePostErrorActivityWithReturnObject() {
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            String testReturn = testService.invokePrePostErrorActivityWithReturnObject();
//            log.info("testReturn: {}", testReturn);
//        });
//        assertEquals(messageStack.size(), 2);
//        assertEquals(exception.getMessage(), "I am a runtime exception message!");
//    }


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
