package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;

public class Message implements Serializable {
    private String action;
    private String date;
    private String time;
    private String dateTime;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "action='" + action + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
