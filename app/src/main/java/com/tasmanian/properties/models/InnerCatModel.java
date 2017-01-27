package com.tasmanian.properties.models;

/**
 * Created by w7u on 12/16/2016.
 */

public class InnerCatModel {

    String innerName;
    String innerImage;
    String innerId;
    String sucatId;
    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    public String getInnerImage() {
        return innerImage;
    }

    public void setInnerImage(String innerImage) {
        this.innerImage = innerImage;
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public String getSucatId() {
        return sucatId;
    }

    public void setSucatId(String sucatId) {
        this.sucatId = sucatId;
    }

    @Override
    public String toString() {
        return "InnerCatModel{" +
                "innerId='" + innerId + '\'' +
                '}';
    }
}
