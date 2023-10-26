package com.example.knowledgeplanet.bean;

import androidx.annotation.NonNull;

public class TuberBean {
    private String tuberName;
    private int tuberImgSource;
    private String tuberDescription;

    public TuberBean() {
    }
    public TuberBean(String TuberName, int imgSource, String description) {
        tuberName=TuberName;
        tuberImgSource=imgSource;
        tuberDescription= description;
    }

    public String getTuberName() {
        return tuberName;
    }

    public void setTuberName(String tuberName) {
        this.tuberName = tuberName;
    }

    public int getTuberImgSource() {
        return tuberImgSource;
    }

    public void setTuberImgSource(int tuberImgSource) {
        this.tuberImgSource = tuberImgSource;
    }

    public String getTuberDescription() {
        return tuberDescription;
    }

    public void setTuberDescription(String tuberDescription) {
        this.tuberDescription = tuberDescription;
    }

    @NonNull
    @Override
    public String toString() {
        return "TuberBean{" +
                "tuberName='" + tuberName + '\'' +
                ", tuberImgSource=" + tuberImgSource +'\''+
                ", tuberDescription='" + tuberDescription + '\'' +
                '}';
    }
}
