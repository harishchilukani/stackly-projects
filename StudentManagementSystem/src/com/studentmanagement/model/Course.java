package com.studentmanagement.model;

public class Course {

    private int courseId;
    private String courseName;
    private int duration;

    public Course(int courseId, String courseName, int duration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.duration = duration;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Course ID: " + courseId +
                ", Course Name: " + courseName +
                ", Duration: " + duration + " months";
    }
}

