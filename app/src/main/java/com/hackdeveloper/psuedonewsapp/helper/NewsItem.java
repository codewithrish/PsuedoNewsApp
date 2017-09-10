package com.hackdeveloper.psuedonewsapp.helper;

import android.support.annotation.NonNull;

public class NewsItem implements Comparable{

    private String ID;
    private String TITLE;
    private String URL;
    private String PUBLISHER;
    private String CATEGORY;
    private String HOSTNAME;
    private boolean FAVOURITE;
    long TIMESTAMP;

    public NewsItem() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPUBLISHER() {
        return PUBLISHER;
    }

    public void setPUBLISHER(String PUBLISHER) {
        this.PUBLISHER = PUBLISHER;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getHOSTNAME() {
        return HOSTNAME;
    }

    public void setHOSTNAME(String HOSTNAME) {
        this.HOSTNAME = HOSTNAME;
    }

    public long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(long TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public boolean isFAVOURITE() {
        return FAVOURITE;
    }

    public void setFAVOURITE(boolean FAVOURITE) {
        this.FAVOURITE = FAVOURITE;
    }

    @Override
    public int compareTo(Object o) {
        long compareTimeStamp = getTIMESTAMP();
        return (int) compareTimeStamp;
    }
}