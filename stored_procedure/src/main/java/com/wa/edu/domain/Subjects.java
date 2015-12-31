package com.wa.edu.domain;

import java.io.Serializable;


public class Subjects implements Serializable {

    private  Long id;

    private  Long version;

    private  String subjectName;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "Subjects{" +
                "id=" + id +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
