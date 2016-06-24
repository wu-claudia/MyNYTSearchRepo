package com.claudiawu.nytimessearch.models;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Filter{
    String beginDate;
    ArrayList<String> array_news_desk;
    String spinnerVal;

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

    public String getDate() {
        return beginDate;
    }

    public String getSpinnerVal() {
        return spinnerVal;
    }

    public void setArray_news_desk(boolean art, boolean fashion, boolean sports) {
        array_news_desk = new ArrayList<>();
        if (art) {
            array_news_desk.add("Arts");
        }
        if (sports) {
            array_news_desk.add("Sports");
        }
        if (fashion) {
            array_news_desk.add("Fashion & Style");
        }
        news_desk = "news_desk:(";
        if (array_news_desk.size() !=0) {
            for (String item: array_news_desk) {
                news_desk += item + " ";
            }
        }
        //news_desk.trim();
        news_desk += ")";
    }

    public ArrayList<String> getArrayNewsDesk() {
        return array_news_desk;
    }

    public String getNewsDesk() {
        return news_desk;
    }

    public void setDate(String date) {
        this.beginDate = date;
    }

    public void setSpinnerVal(String spinner) {
        this.spinnerVal = spinner;
    }

    boolean isArt;
    boolean isSports;
    boolean isFashion;

    public void setArt(boolean art) {
        isArt = art;
    }

    public void setSports(boolean sports) {
        isSports = sports;
    }

    public void setFashion(boolean fashion) {
        isFashion = fashion;
    }

    public Filter() {
        news_desk = "";
    }

    public Filter(String beginDate, boolean isArt,boolean isSports,boolean isFashion ) {
        this.beginDate = beginDate;
        this.isArt = isArt;
        this.isSports = isSports;
        this.isFashion = isFashion;
        this.array_news_desk = new ArrayList<>();
    }



}
