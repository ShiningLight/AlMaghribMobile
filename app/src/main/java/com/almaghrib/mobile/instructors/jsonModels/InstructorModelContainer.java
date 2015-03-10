package com.almaghrib.mobile.instructors.jsonModels;

public class InstructorModelContainer {
    String name;
    String role;
    String city;
    String description;

    public InstructorModelContainer(String name, String role, String city, String description) {
        this.name = name;
        this.role = role;
        this.city = city;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }
}
