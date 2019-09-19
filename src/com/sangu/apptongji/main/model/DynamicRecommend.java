package com.sangu.apptongji.main.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class DynamicRecommend implements Serializable {

    JSONObject object;
    Double margin;


    public void setObject(JSONObject object) {
        this.object = object;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getMargin() {
        return margin;
    }
}
