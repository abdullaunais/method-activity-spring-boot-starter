package io.github.abdullaunais.methodactivity.core.domain;

public enum ActivityLevel {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, "TRACE");


    private final int levelInt;
    private final String levelStr;

    ActivityLevel(int i, String s) {
        this.levelInt = i;
        this.levelStr = s;
    }

    public static ActivityLevel intToLevel(int levelInt) {
        return switch (levelInt) {
            case 0 -> TRACE;
            case 10 -> DEBUG;
            case 20 -> INFO;
            case 30 -> WARN;
            case 40 -> ERROR;
            default -> throw new IllegalArgumentException("Level integer [" + levelInt + "] not recognized.");
        };
    }

    public int toInt() {
        return this.levelInt;
    }

    public String toString() {
        return this.levelStr;
    }
}
