package com.power.oj.core.bean;

public class ErrorFlashMessage extends FlashMessage {

    public ErrorFlashMessage(String content) {
        super(content, MessageType.ERROR, "Error");
    }

    public ErrorFlashMessage(String content, String title) {
        super(content, MessageType.ERROR, title);
    }

}
