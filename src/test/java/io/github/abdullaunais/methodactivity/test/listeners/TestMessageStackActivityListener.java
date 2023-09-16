package io.github.abdullaunais.methodactivity.test.listeners;

import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import io.github.abdullaunais.methodactivity.core.event.ActivityEventListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Slf4j
@Component
public class TestMessageStackActivityListener implements ActivityEventListener {
    private final Stack<String> messageStack;

    public TestMessageStackActivityListener(Stack<String> messageStack) {
        this.messageStack = messageStack;
    }

    @Override
    public void onPreActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        "PreActivity",
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
        messageStack.push(parsedActivity.getActivity());
    }

    @Override
    public void onPostActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        "PostActivity",
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
        messageStack.push(parsedActivity.getActivity());
    }

    @Override
    public void onErrorActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        "ErrorActivity",
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
        messageStack.push(parsedActivity.getActivity());
    }
}
