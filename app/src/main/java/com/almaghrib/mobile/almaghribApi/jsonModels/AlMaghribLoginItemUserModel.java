package com.almaghrib.mobile.almaghribApi.jsonModels;

public class AlMaghribLoginItemUserModel {
    String email;
    String fullName;
    String prefName;
    String country;
    String gender;
    String birthDate;


    public AlMaghribLoginItemUserModel(String email, String fullName, String prefName, String country, String gender, String birthDate) {
        this.email = email;
        this.fullName = fullName;
        this.prefName = prefName;
        this.country = country;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPrefName() {
        return prefName;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "AlMaghribLoginItemUserModel{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", prefName='" + prefName + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }
}
