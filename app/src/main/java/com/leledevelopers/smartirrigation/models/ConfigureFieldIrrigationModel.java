package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;

public class ConfigureFieldIrrigationModel implements Serializable {
    private int fieldNo;
    private int valveOnPeriod;
    private int valveOffPeriod;
    private int soilDryness;
    private int soilWetness;
    private String motorOnTime;
    private int priority;
    private int cycle;
    private String tiggerFrom;
    private int motorOnTimeHr;
    private int motorOnTimeMins;
    private boolean isEnabled = false;

    public int getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(int fieldNo) {
        this.fieldNo = fieldNo;
    }

    public int getValveOnPeriod() {
        return valveOnPeriod;
    }

    public void setValveOnPeriod(int valveOnPeriod) {
        this.valveOnPeriod = valveOnPeriod;
    }

    public int getValveOffPeriod() {
        return valveOffPeriod;
    }

    public void setValveOffPeriod(int valveOffPeriod) {
        this.valveOffPeriod = valveOffPeriod;
    }

    public int getSoilDryness() {
        return soilDryness;
    }

    public void setSoilDryness(int soilDryness) {
        this.soilDryness = soilDryness;
    }

    public String getMotorOnTime() {
        return motorOnTime;
    }

    public void setMotorOnTime(String motorOnTime) {
        this.motorOnTime = motorOnTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getTiggerFrom() {
        return tiggerFrom;
    }

    public void setTiggerFrom(String tiggerFrom) {
        this.tiggerFrom = tiggerFrom;
    }

    public int getSoilWetness() {
        return soilWetness;
    }

    public void setSoilWetness(int soilWetness) {
        this.soilWetness = soilWetness;
    }

    public int getMotorOnTimeHr() {
        return motorOnTimeHr;
    }

    public void setMotorOnTimeHr(int motorOnTimeHr) {
        this.motorOnTimeHr = motorOnTimeHr;
    }

    public int getMotorOnTimeMins() {
        return motorOnTimeMins;
    }

    public void setMotorOnTimeMins(int motorOnTimeMins) {
        this.motorOnTimeMins = motorOnTimeMins;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return "ConfigureFieldIrrigationModel{" +
                "fieldNo=" + fieldNo +
                ", valveOnPeriod=" + valveOnPeriod +
                ", valveOffPeriod=" + valveOffPeriod +
                ", soilDryness=" + soilDryness +
                ", soilWetness=" + soilWetness +
                ", motorOnTime='" + motorOnTime + '\'' +
                ", priority=" + priority +
                ", cycle=" + cycle +
                ", tiggerFrom='" + tiggerFrom + '\'' +
                ", motorOnTimeHr=" + motorOnTimeHr +
                ", motorOnTimeMins=" + motorOnTimeMins +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
