package com.codepath.week2assignment.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by lin1000 on 2017/2/24.
 */

@Parcel
public class Meta {
    int hits;
    int time;
    int offset;

    public Meta(){};

    public Meta(JSONObject jsonObject) throws JSONException {
        hits = jsonObject.getInt("hits");
        time = jsonObject.getInt("time");
        offset =  jsonObject.getInt("offset");
    }

    public int getHits() {
        return hits;
    }

    public int getTime() {
        return time;
    }

    public int getOffset() {
        return offset;
    }
}
