
package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Appointment(int appointmentId, int patientId, int doctorId,
                       LocalDate date, LocalTime time) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = "BOOKED";
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentId
                + ", Patient ID: " + patientId
                + ", Doctor ID: " + doctorId
                + ", Date: " + date
                + ", Time: " + time
                + ", Status: " + status;
    }
}