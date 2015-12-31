package com.wa.edu.domain;

import java.io.Serializable;


public class TextBookVersion implements Serializable {

    public String getTextBookVerionName() {
        return textBookVerionName;
    }

    public void setTextBookVerionName(String textBookVerionName) {
        this.textBookVerionName = textBookVerionName;
    }

    private String textBookVerionName;
}
