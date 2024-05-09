package com.voeacademy.application;

import java.io.Serializable;
import java.util.List;

public class SubjectTeacher implements Serializable {
    private String imageUrl;
    private String teacherID,name;
private String rating;
private List<String> subjectList;

    public SubjectTeacher(String teacherID, String imageUrl, String name, String rating, List<String> subjectList) {
        this.teacherID = teacherID;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
        this.subjectList=subjectList;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
