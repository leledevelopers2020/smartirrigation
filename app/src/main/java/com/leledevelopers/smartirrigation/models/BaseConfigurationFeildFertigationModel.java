package com.leledevelopers.smartirrigation.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BaseConfigurationFeildFertigationModel implements Serializable {
    private List<ConfigurationFeildFertigationModel> modelList = new LinkedList<ConfigurationFeildFertigationModel>();
    private int lastEnabledFieldNo = -1;

    public List<ConfigurationFeildFertigationModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ConfigurationFeildFertigationModel> modelList) {
        this.modelList = modelList;
    }

    public int getLastEnabledFieldNo() {
        return lastEnabledFieldNo;
    }

    public void setLastEnabledFieldNo(int lastEnabledFieldNo) {
        this.lastEnabledFieldNo = lastEnabledFieldNo;
    }
}
