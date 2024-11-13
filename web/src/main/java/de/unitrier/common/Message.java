package de.unitrier.common;

import java.io.Serializable;

public class Message implements Serializable {

    private final String text;
    private final int id;
    private long timestamp;


    public Message(String text, int id) {
        this.text = text;
        this.id = id;
        timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
