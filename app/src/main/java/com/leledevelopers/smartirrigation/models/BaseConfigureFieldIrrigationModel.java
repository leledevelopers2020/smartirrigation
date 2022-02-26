package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BaseConfigureFieldIrrigationModel implements Serializable {
    private List<ConfigureFieldIrrigationModel> modelList = new LinkedList<ConfigureFieldIrrigationModel>();
    private int lastEnabledFieldNo = -1;

    public List<ConfigureFieldIrrigationModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ConfigureFieldIrrigationModel> modelList) {
        this.modelList = modelList;
    }

    public int getLastEnabledFieldNo() {
        return lastEnabledFieldNo;
    }

    public void setLastEnabledFieldNo(int lastEnabledFieldNo) {
        this.lastEnabledFieldNo = lastEnabledFieldNo;
    }
}
