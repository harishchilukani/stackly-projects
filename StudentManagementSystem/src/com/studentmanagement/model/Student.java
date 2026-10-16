package com.studentmanagement.model;

public class Student {

    private int id;
    private String name;
    private String email;
    private double marks;
    private int attendance;
    private StudentStatus status;
    private Course course;

    public Student(int id, String name, String email, double marks,
                   int attendance, StudentStatus status, Course course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.marks = marks;
        this.attendance = attendance;
        this.status = status;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getMarks() {
        return marks;
    }

    public int getAttendance() {
        return attendance;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public Course getCourse() {
        return course;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", Email: " + email +
                ", Marks: " + marks +
                ", Attendance: " + attendance +
                ", Status: " + status +
                ", Course: " + course.getCourseName();
    }
}

