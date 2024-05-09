package com.voeacademy.application;

import com.google.firebase.Timestamp;

public class PaymentNotification {
    private String userName , userMoblileNo , userEmail , userID;
    private String docID , teacherName , no_of_classes , amount , paymentID , subject , selectedClass , conform , classStatus;
    Timestamp timeStamp;
    Boolean check_read , isClassesCompleted;

    public PaymentNotification(String docID , String userName, String userMoblileNo, String userEmail, String userID, String teacherName, String no_of_classes, String amount, String paymentID, String subject, String selectedClass, Timestamp timeStamp , Boolean check_read , Boolean isClassesCompleted , String classStatus , String conform) {
        this.docID = docID;
        this.userName = userName;
        this.userMoblileNo = userMoblileNo;
        this.userEmail = userEmail;
        this.userID = userID;
        this.teacherName = teacherName;
        this.no_of_classes = no_of_classes;
        this.amount = amount;
        this.paymentID = paymentID;
        this.subject = subject;
        this.selectedClass = selectedClass;
        this.timeStamp = timeStamp;
        this.check_read = check_read;
        this.isClassesCompleted = isClassesCompleted;
        this.classStatus = classStatus;
        this.conform = conform;
    }

    public String getDocID() {
        return docID;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMoblileNo() {
        return userMoblileNo;
    }

    public void setUserMoblileNo(String userMoblileNo) {
        this.userMoblileNo = userMoblileNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getNo_of_classes() {
        return no_of_classes;
    }

    public void setNo_of_classes(String no_of_classes) {
        this.no_of_classes = no_of_classes;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getCheck_read() {
        return check_read;
    }

    public Boolean getIsClassesCompleted() {
        return isClassesCompleted;
    }

    public String getConform() {
        return conform;
    }

    public void setConform(String conform) {
        this.conform = conform;
    }

    public String getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(String classStatus) {
        this.classStatus = classStatus;
    }
}
