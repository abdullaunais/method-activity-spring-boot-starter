package com.github.abu.methodactivity.test.providers;

import com.github.abu.methodactivity.activity.annotations.provider.ActivityProvider;
import com.github.abu.methodactivity.activity.domain.ActivityLevel;
import com.github.abu.methodactivity.activity.domain.ParsedActivity;
import com.github.abu.methodactivity.activity.provider.IActivityProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Stack;

@Slf4j
@ActivityProvider
public class TestMessageStackActivityProvider implements IActivityProvider {
    private final Stack<String> messageStack;

    public TestMessageStackActivityProvider(Stack<String> messageStack) {
        this.messageStack = messageStack;
    }


    @Override
    public void send(ParsedActivity<?> parsedActivity, Annotation annotation, ActivityLevel level) {
        messageStack.push(parsedActivity.getActivity());
    }
}
