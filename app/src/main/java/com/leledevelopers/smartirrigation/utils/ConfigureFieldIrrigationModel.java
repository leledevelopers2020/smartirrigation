package com.leledevelopers.smartirrigation.utils;

public class ConfigureFieldIrrigationModel {
    private int fieldNo;
    private int valveOnPeriod;
    private int valveOffPeriod;
    private int soilDryness;
    private String motorOnTime;
    private int priority;
    private int cycle;
    private String tiggerFrom;

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
}
