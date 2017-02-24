package com.codepath.week2assignment.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by lin1000 on 2017/2/24.
 */

@Parcel
public class NYTMultimedia {
    int width;
    String url;
    int height;
    String subtype;
    //String legacyWide;
    //int legacyWideHeight;
    //int legacyWideWidth;
    String type;

    public NYTMultimedia(){};

    public NYTMultimedia(JSONObject jsonObject) throws JSONException {
        width = jsonObject.getInt("width");
        url = jsonObject.getString("url");
        height = jsonObject.getInt("height");
        subtype = jsonObject.getString("subtype");
        type = jsonObject.getString("type");
    }

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return "http://www.nytimes.com/" + url;
    }

    public int getHeight() {
        return height;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getType() {
        return type;
    }
}
