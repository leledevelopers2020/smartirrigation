package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;

public class FiltrationModel implements Serializable {
    private int fcDelay_1;
    private int fcDelay_2;
    private int fcDelay_3;
    private int fcOnTime;
    private int fcSeperation;

    public int getFcDelay_1() {
        return fcDelay_1;
    }

    public void setFcDelay_1(int fcDelay_1) {
        this.fcDelay_1 = fcDelay_1;
    }

    public int getFcDelay_2() {
        return fcDelay_2;
    }

    public void setFcDelay_2(int fcDelay_2) {
        this.fcDelay_2 = fcDelay_2;
    }

    public int getFcDelay_3() {
        return fcDelay_3;
    }

    public void setFcDelay_3(int fcDelay_3) {
        this.fcDelay_3 = fcDelay_3;
    }

    public int getFcOnTime() {
        return fcOnTime;
    }

    public void setFcOnTime(int fcOnTime) {
        this.fcOnTime = fcOnTime;
    }

    public int getFcSeperation() {
        return fcSeperation;
    }

    public void setFcSeperation(int fcSeperation) {
        this.fcSeperation = fcSeperation;
    }

    @Override
    public String toString() {
        return "FiltrationModel{" +
                "fcDelay_1=" + fcDelay_1 +
                ", fcDelay_2=" + fcDelay_2 +
                ", fcDelay_3=" + fcDelay_3 +
                ", fcOnTime=" + fcOnTime +
                ", fcSeperation=" + fcSeperation +
                '}';
    }
}
