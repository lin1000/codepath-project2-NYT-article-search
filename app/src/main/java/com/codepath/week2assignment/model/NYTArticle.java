package com.codepath.week2assignment.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by lin1000 on 2017/2/24.
 */

@Parcel
public class NYTArticle {
    String webUrl;
    String snippet;
    String leadParagraph;
    String abs;
    String printPage;
    String source;
    NYTMultimedia nytMultimedia;
    String headLine;

    public NYTArticle(){};

    public NYTArticle(JSONObject jsonObject) throws JSONException {
        webUrl = jsonObject.getString("web_url");
        snippet = jsonObject.getString("snippet");
        leadParagraph =  jsonObject.getString("lead_paragraph");
        abs = jsonObject.getString("abstract");
        printPage = jsonObject.getString("print_page");
        source = jsonObject.getString("source");
        JSONArray multimedia = jsonObject.getJSONArray("multimedia");
        if (multimedia.length()>0){
            JSONObject multimediaJsonObject = multimedia.getJSONObject(0);
            NYTMultimedia nytMultimedia = new NYTMultimedia(multimediaJsonObject);
            this.nytMultimedia = nytMultimedia;
        } else{
            nytMultimedia = null;
        }
        headLine = jsonObject.getJSONObject("headline").getString("main");

    }

    public static ArrayList<NYTArticle> fromJSONArray(JSONArray array){
        ArrayList<NYTArticle> results = new ArrayList<>();
        for (int x=0 ; x < array.length(); x++){
            try {
                results.add(new NYTArticle(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getLeadParagraph() {
        return leadParagraph;
    }

    public String getAbs() {
        return abs;
    }

    public String getPrintPage() {
        return printPage;
    }

    public String getSource() {
        return source;
    }

    public NYTMultimedia getNytMultimedia() {
        return nytMultimedia;
    }

    public String getHeadLine() {
        return headLine;
    }
}
