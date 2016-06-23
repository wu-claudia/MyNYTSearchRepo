package com.claudiawu.nytimessearch.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by claudiawu on 6/22/16.
 */
public class Filter implements Serializable{
    String beginDate;
    ArrayList<String> array_news_desk;

    public String getNews_desk() {
        return news_desk;
    }

    String news_desk;

    public String getBeginDate() {
        return beginDate;
    }

    public boolean isArt() {
        return isArt;
    }

    public boolean isSports() {
        return isSports;
    }

    public boolean isFashion() {
        return isFashion;
    }

    boolean isArt;
    boolean isSports;
    boolean isFashion;

    public Filter() {

    }

    public Filter(String beginDate, boolean isArt,boolean isSports,boolean isFashion ) {
        this.beginDate = beginDate;
        this.isArt = isArt;
        this.isSports = isSports;
        this.isFashion = isFashion;

        if (isArt) {
            array_news_desk.add("Art");
        }
        if (isSports) {
            array_news_desk.add("Sports");
        }
        if (isFashion) {
            array_news_desk.add("Fashion");
        }

        news_desk = "";
        for (String item: array_news_desk) {
            news_desk += item + " ";
        }
        news_desk.trim();
    }



}
