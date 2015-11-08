package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribLoginItemUserModel {
    String email;
    String name_full;
    String name_pref;
    String country_id;
    String gender;
    String birth_date;
    String email_md5;


    public AlMaghribLoginItemUserModel(String email, String fullName, String prefName,
                                       String country, String gender, String birthDate,
                                       String emailMd5) {
        this.email = email;
        this.name_full = fullName;
        this.name_pref = prefName;
        this.country_id = country;
        this.gender = gender;
        this.birth_date = birthDate;
        this.email_md5 = emailMd5;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return name_full;
    }

    public String getPrefName() {
        return name_pref;
    }

    public String getCountry() {
        return country_id;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birth_date;
    }

    public String getEmailMd5() {
        return email_md5;
    }

    @Override
    public String toString() {
        return "AlMaghribLoginItemUserModel{" +
                "email='" + email + '\'' +
                ", fullName='" + name_full + '\'' +
                ", prefName='" + name_pref + '\'' +
                ", country='" + country_id + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birth_date + '\'' +
                ", emailMd5='" + email_md5 + '\'' +
                '}';
    }
}
