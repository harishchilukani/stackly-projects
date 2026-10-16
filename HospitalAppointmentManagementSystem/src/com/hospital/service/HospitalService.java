
package com.hospital.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hospital.model.Patient;
import com.hospital.model.Doctor;
import com.hospital.model.Appointment;

import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.exception.SlotAlreadyBookedException;
import com.hospital.exception.InvalidAppointmentSlotException;
import com.hospital.exception.ValidationException;

public class HospitalService {

    private Map<Integer, Patient> patients = new HashMap<>();
    private Map<Integer, Doctor> doctors = new HashMap<>();
    private Map<String, Appointment> appointments = new HashMap<>();

    private ArrayList<LocalTime> slots = new ArrayList<>();

    public HospitalService() {

        slots.add(LocalTime.of(9, 0));
        slots.add(LocalTime.of(9, 30));
        slots.add(LocalTime.of(10, 0));
        slots.add(LocalTime.of(10, 30));
        slots.add(LocalTime.of(11, 0));
        slots.add(LocalTime.of(11, 30));
        slots.add(LocalTime.of(12, 0));
        slots.add(LocalTime.of(12, 30));
        slots.add(LocalTime.of(13, 0));
        slots.add(LocalTime.of(13, 30));
        slots.add(LocalTime.of(14, 0));
        slots.add(LocalTime.of(14, 30));
        slots.add(LocalTime.of(15, 0));
        slots.add(LocalTime.of(15, 30));
        slots.add(LocalTime.of(16, 0));
        slots.add(LocalTime.of(16, 30));
        slots.add(LocalTime.of(17, 0));
        slots.add(LocalTime.of(17, 30));
        slots.add(LocalTime.of(18, 0));
    }

    // Register Patient
    public void registerPatient(Patient patient) {

        if (patient.getPatientId() <= 0) {
            throw new ValidationException("Patient ID must be positive");
        }

        if (patient.getName() == null || patient.getName().isBlank()) {
            throw new ValidationException("Patient name cannot be empty");
        }

        if (patient.getAge() < 1 || patient.getAge() > 100) {
            throw new ValidationException("Age must be between 1 and 120");
        }

        if (patient.getGender() == null || patient.getGender().isBlank()) {
            throw new ValidationException("Gender cannot be empty");
        }

        if (patient.getPhone() == null ||
                !patient.getPhone().matches("\\d{10}")) {
            throw new ValidationException(
                    "Phone number must contain exactly 10 digits");
        }

        if (patients.containsKey(patient.getPatientId())) {
            throw new ValidationException("Patient ID already exists");
        }

        patients.put(patient.getPatientId(), patient);
        System.out.println("Patient registered successfully");
    }

    // Add Doctor
    public void addDoctor(Doctor doctor) {

        if (doctor.getDoctorId() <= 0) {
            throw new ValidationException("Doctor ID must be positive");
        }

        if (doctor.getName() == null || doctor.getName().isBlank()) {
            throw new ValidationException("Doctor name cannot be empty");
        }

        if (doctor.getSpecialization() == null ||
                doctor.getSpecialization().isBlank()) {
            throw new ValidationException(
                    "Specialization cannot be empty");
        }

        if (doctors.containsKey(doctor.getDoctorId())) {
            throw new ValidationException("Doctor ID already exists");
        }

        doctors.put(doctor.getDoctorId(), doctor);
        System.out.println("Doctor added successfully");
    }

    // Display All Patients
    public void displayPatients() {

        if (patients.isEmpty()) {
            System.out.println("No patients registered");
            return;
        }

        for (Patient patient : patients.values()) {
            System.out.println(patient);
        }
    }

    // Display All Doctors
    public void displayDoctors() {

        if (doctors.isEmpty()) {
            System.out.println("No doctors available");
            return;
        }

        for (Doctor doctor : doctors.values()) {
            System.out.println(doctor);
        }
    }

    // Book Appointment
    public void bookAppointment(int appointmentId, int patientId,
                                int doctorId, LocalDate date,
                                LocalTime time) {

        if (appointmentId <= 0) {
            throw new ValidationException(
                    "Appointment ID must be positive");
        }

        if (appointments.containsKey(String.valueOf(appointmentId))) {
            throw new ValidationException(
                    "Appointment ID already exists");
        }

        if (!patients.containsKey(patientId)) {
            throw new PatientNotFoundException(
                    "Patient ID " + patientId + " not found");
        }

        if (!doctors.containsKey(doctorId)) {
            throw new DoctorNotFoundException(
                    "Doctor ID " + doctorId + " not found");
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "Appointment date cannot be in the past");
        }

        if (time == null || !slots.contains(time)) {
            throw new InvalidAppointmentSlotException(
                    "Invalid time slot");
        }

        // Check whether the doctor's slot is already booked
        for (Appointment appointment : appointments.values()) {

            if (appointment.getDoctorId() == doctorId
                    && appointment.getDate().equals(date)
                    && appointment.getTime().equals(time)
                    && appointment.getStatus().equals("BOOKED")) {

                throw new SlotAlreadyBookedException(
                        "This doctor already has an appointment "
                                + "at this date and time");
            }
        }

        Appointment appointment = new Appointment(
                appointmentId, patientId, doctorId, date, time);

        appointments.put(String.valueOf(appointmentId), appointment);

        System.out.println("Appointment booked successfully");
        System.out.println(appointment);
    }

    // View Available Slots
    public void viewAvailableSlots(int doctorId, LocalDate date) {

        if (!doctors.containsKey(doctorId)) {
            throw new DoctorNotFoundException(
                    "Doctor ID " + doctorId + " not found");
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "Date cannot be in the past");
        }

        System.out.println("Available slots for Doctor ID: "
                + doctorId + " on " + date);

        boolean found = false;

        for (LocalTime slot : slots) {

            boolean booked = false;

            for (Appointment appointment : appointments.values()) {

                if (appointment.getDoctorId() == doctorId
                        && appointment.getDate().equals(date)
                        && appointment.getTime().equals(slot)
                        && appointment.getStatus().equals("BOOKED")) {

                    booked = true;
                    break;
                }
            }

            if (!booked) {
                System.out.println(slot);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available slots");
        }
    }

    // View Doctor Schedule
    public void viewDoctorSchedule(int doctorId, LocalDate date) {

        Doctor doctor = doctors.get(doctorId);

        if (doctor == null) {
            throw new DoctorNotFoundException(
                    "Doctor ID " + doctorId + " not found");
        }

        if (date == null) {
            throw new ValidationException("Date cannot be empty");
        }

        System.out.println("Schedule for " + doctor.getName()
                + " on " + date);

        boolean found = false;

        for (Appointment appointment : appointments.values()) {

            if (appointment.getDoctorId() == doctorId
                    && appointment.getDate().equals(date)
                    && appointment.getStatus().equals("BOOKED")) {

                Patient patient = patients.get(
                        appointment.getPatientId());

                System.out.println("Time: " + appointment.getTime());
                System.out.println("Patient: " + patient.getName());
                System.out.println("Appointment ID: "
                        + appointment.getAppointmentId());
                System.out.println("-------------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments scheduled");
        }
    }

    // Cancel Appointment
    public void cancelAppointment(int appointmentId) {

        Appointment appointment = appointments.get(
                String.valueOf(appointmentId));

        if (appointment == null) {
            throw new ValidationException(
                    "Appointment ID " + appointmentId + " not found");
        }

        if (appointment.getStatus().equals("CANCELLED")) {
            throw new ValidationException(
                    "Appointment is already cancelled");
        }

        appointment.setStatus("CANCELLED");

        System.out.println("Appointment cancelled successfully");
    }

    // View Patient History
    public void viewPatientHistory(int patientId) {

        Patient patient = patients.get(patientId);

        if (patient == null) {
            throw new PatientNotFoundException(
                    "Patient ID " + patientId + " not found");
        }

        System.out.println("Appointment history for: "
                + patient.getName());

        boolean found = false;

        for (Appointment appointment : appointments.values()) {

            if (appointment.getPatientId() == patientId) {

                System.out.println(appointment);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointment history found");
        }
    }
}