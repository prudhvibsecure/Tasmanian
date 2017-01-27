package com.tasmanian.properties.models;

/**
 * Created by w7u on 12/20/2016.
 */

public class SearchPlace {

    String description;
    String place_id;
    String reference;
    @Override
    public String toString() {
        return  place_id;
    }



    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
