package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseMessages implements Serializable {
    private List<Message> messages = new ArrayList<Message>();
    private String lastAccessedDate = "";
    private boolean isMessageInitial = true;
    private List<Message> rtcMessages = new ArrayList<Message>();
    private String rtcLastMessageDate = "";
    private boolean isRTCMessageInitial = true;

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

    public boolean isMessageInitial() {
        return isMessageInitial;
    }

    public void setMessageInitial(boolean messageInitial) {
        isMessageInitial = messageInitial;
    }

    public List<Message> getRtcMessages() {
        return rtcMessages;
    }

    public void setRtcMessages(List<Message> rtcMessages) {
        this.rtcMessages = rtcMessages;
    }

    public String getRtcLastMessageDate() {
        return rtcLastMessageDate;
    }

    public void setRtcLastMessageDate(String rtcLastMessageDate) {
        this.rtcLastMessageDate = rtcLastMessageDate;
    }

    public boolean isRTCMessageInitial() {
        return isRTCMessageInitial;
    }

    public void setRTCMessageInitial(boolean RTCMessageInitial) {
        isRTCMessageInitial = RTCMessageInitial;
    }

    @Override
    public String toString() {
        return "BaseMessages{" +
                "messages=" + messages +
                ", lastAccessedDate='" + lastAccessedDate + '\'' +
                ", isMessageInitial=" + isMessageInitial +
                ", rtcMessages=" + rtcMessages +
                ", rtcLastMessageDate='" + rtcLastMessageDate + '\'' +
                ", isRTCMessageInitial=" + isRTCMessageInitial +
                '}';
    }
}
