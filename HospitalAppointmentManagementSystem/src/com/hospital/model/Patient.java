
package com.hospital.model;

public class Patient {

    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String phone;

    public Patient(int patientId, String name, int age,
                   String gender, String phone) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId
                + ", Name: " + name
                + ", Age: " + age
                + ", Gender: " + gender
                + ", Phone: " + phone;
    }
}