package com.example.lab3;

public class Message {

    private String chatMessage;
    private int isLeft;
    private long id;

    public Message(){
        super();
    }

    public Message(String chatMessage, int isLeft, long id){
        super();
        this.chatMessage = chatMessage;
        this.isLeft = isLeft;
        this.id = id;
    }


    public String getMessage(){
        return chatMessage;
    }

    public void setType(int isLeft) {
        this.isLeft = isLeft;
    }

    public int getType() {
        return isLeft;
    }

    public long getID()
    {
        return id;
    }
}
