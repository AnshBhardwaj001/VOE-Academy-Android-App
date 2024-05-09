package com.voeacademy.application;

public class NewUserDataHolder {
    String name,email,gender,current_class,mobile_no,date_of_account_creation;
    boolean profile_verification;

    public NewUserDataHolder(String name, String email, String gender, String current_class, String mobile_no, String date_of_account_creation, boolean profile_verification) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.current_class = current_class;
        this.mobile_no = mobile_no;
        this.date_of_account_creation = date_of_account_creation;
        this.profile_verification = profile_verification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCurrent_class() {
        return current_class;
    }

    public void setCurrent_class(String current_class) {
        this.current_class = current_class;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public boolean isProfile_verification() {
        return profile_verification;
    }

    public void setProfile_verification(boolean profile_verification) {
        this.profile_verification = profile_verification;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getDate_of_account_creation() {
        return date_of_account_creation;
    }

    public void setDate_of_account_creation(String date_of_account_creation) {
        this.date_of_account_creation = date_of_account_creation;
    }
}
