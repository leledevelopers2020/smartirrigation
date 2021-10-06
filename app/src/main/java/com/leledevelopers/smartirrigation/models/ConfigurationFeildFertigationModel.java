package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;

public class ConfigurationFeildFertigationModel implements Serializable {
    private int fieldNo;
    private int wetPeriod;
    private int injectPeriod;
    private int noIterations;

    public int getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(int fieldNo) {
        this.fieldNo = fieldNo;
    }

    public int getWetPeriod() {
        return wetPeriod;
    }

    public void setWetPeriod(int wetPeriod) {
        this.wetPeriod = wetPeriod;
    }

    public int getInjectPeriod() {
        return injectPeriod;
    }

    public void setInjectPeriod(int injectPeriod) {
        this.injectPeriod = injectPeriod;
    }

    public int getNoIterations() {
        return noIterations;
    }

    public void setNoIterations(int noIterations) {
        this.noIterations = noIterations;
    }

    @Override
    public String toString() {
        return "ConfigurationFeildFertigationModel{" +
                "fieldNo=" + fieldNo +
                ", wetPeriod=" + wetPeriod +
                ", injectPeriod=" + injectPeriod +
                ", noIterations=" + noIterations +
                '}';
    }
}
