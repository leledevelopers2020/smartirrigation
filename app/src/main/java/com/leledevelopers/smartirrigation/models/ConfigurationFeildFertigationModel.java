package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;

public class ConfigurationFeildFertigationModel implements Serializable {
    private int fieldNo;
    private int wetPeriod;
    private int injectPeriod;
    private int noIterations;
    private boolean isEnabled = false;
    private boolean isModelEmpty = true;

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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isModelEmpty() {
        return isModelEmpty;
    }

    public void setModelEmpty(boolean modelEmpty) {
        isModelEmpty = modelEmpty;
    }

    @Override
    public String toString() {
        return "ConfigurationFeildFertigationModel{" +
                "fieldNo=" + fieldNo +
                ", wetPeriod=" + wetPeriod +
                ", injectPeriod=" + injectPeriod +
                ", noIterations=" + noIterations +
                ", isEnabled=" + isEnabled +
                ", isModelEmpty=" + isModelEmpty +
                '}';
    }
}
