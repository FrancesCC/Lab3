package com.example.lab3;

import android.text.Editable;

public class Message {

    private String chatMessage;
    private boolean isLeft;

    public Message(){
        super();
    }

    public Message(String chatMessage, boolean isLeft){
        super();
        this.chatMessage = chatMessage;
        this.isLeft = isLeft;
    }

    public void setMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getMessage(){
        return chatMessage;
    }

    public void setType(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean getType() {
        return isLeft;
    }
}
