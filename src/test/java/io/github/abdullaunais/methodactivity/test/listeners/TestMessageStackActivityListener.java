package io.github.abdullaunais.methodactivity.test.listeners;

import io.github.abdullaunais.methodactivity.core.domain.ActivityAnnotationData;
import io.github.abdullaunais.methodactivity.core.domain.ActivityType;
import io.github.abdullaunais.methodactivity.core.domain.ParsedActivity;
import io.github.abdullaunais.methodactivity.core.event.IActivityEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Slf4j
@Component
public class TestMessageStackActivityListener implements IActivityEvent {
    private final Stack<String> messageStack;

    public TestMessageStackActivityListener(Stack<String> messageStack) {
        this.messageStack = messageStack;
    }

    @Override
    public void onEvent(ParsedActivity<?> parsedActivity, ActivityType activityType, ActivityAnnotationData annotationData) {
        log.atLevel(Level.intToLevel(parsedActivity.getActivityLevel().toInt()))
                .log("({}) [{}:{}] [{}]: {}",
                        activityType.name(),
                        parsedActivity.getEntity(),
                        parsedActivity.getEntityId(),
                        parsedActivity.getParams(),
                        parsedActivity.getActivity());
        messageStack.push(parsedActivity.getActivity());
    }
}
