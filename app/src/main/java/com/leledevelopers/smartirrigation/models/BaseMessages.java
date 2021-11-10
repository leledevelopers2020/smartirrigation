package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseMessages implements Serializable {
    private List<Message> messages = new ArrayList<Message>();
    private String lastAccessedDate = "";

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getLastAccessedDate() {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(String lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    @Override
    public String toString() {
        return "BaseMessages{" +
                "messages=" + messages +
                ", lastAccessedDate='" + lastAccessedDate + '\'' +
                '}';
    }
}
