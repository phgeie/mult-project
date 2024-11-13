package de.unitrier.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Handshake implements Serializable {

    private int id;
    private ArrayList<Message> messagesList;

    public Handshake(int id, ArrayList<Message> messagesList) {
        this.id = id;
        this.messagesList = messagesList;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Message> getMessagesList() {
        return messagesList;
    }
}
