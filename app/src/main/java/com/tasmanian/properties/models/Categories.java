package com.tasmanian.properties.models;

/**
 * Created by w7u on 12/5/2016.
 */

public class Categories {

    private String cid,catImage,cname;
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return cname;
    }
}
