package com.power.oj.judge;

public class JudgeConfigurationException extends Exception {
    public JudgeConfigurationException() {
        super();
    }

    public JudgeConfigurationException(String message) {
        super(message);
    }

    public JudgeConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JudgeConfigurationException(Throwable cause) {
        super(cause);
    }

    protected JudgeConfigurationException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
