
package com.hospital.app;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import com.hospital.model.Patient;
import com.hospital.model.Doctor;
import com.hospital.service.HospitalService;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.exception.SlotAlreadyBookedException;
import com.hospital.exception.InvalidAppointmentSlotException;
import com.hospital.exception.ValidationException;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HospitalService service = new HospitalService();

        while (true) {

            System.out.println("\n===== HOSPITAL APPOINTMENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Book Appointment");
            System.out.println("4. View Available Slots");
            System.out.println("5. View Doctor Schedule");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. View Patient History");
            System.out.println("8. Display All Patients");
            System.out.println("9. Display All Doctors");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {

                    case 1:
                        System.out.print("Enter Patient ID: ");
                        int patientId = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Age: ");
                        int age = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Gender: ");
                        String gender = scanner.nextLine();

                        System.out.print("Enter Phone: ");
                        String phone = scanner.nextLine();

                        Patient patient = new Patient(
                                patientId, name, age, gender, phone);

                        service.registerPatient(patient);
                        break;

                    case 2:
                        System.out.print("Enter Doctor ID: ");
                        int doctorId = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Doctor Name: ");
                        String doctorName = scanner.nextLine();

                        System.out.print("Enter Specialization: ");
                        String specialization = scanner.nextLine();

                        Doctor doctor = new Doctor(
                                doctorId, doctorName, specialization);

                        service.addDoctor(doctor);
                        break;

                    case 3:
                        System.out.print("Enter Appointment ID: ");
                        int appointmentId = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Patient ID: ");
                        int pId = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Doctor ID: ");
                        int dId = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(scanner.nextLine().trim());

                        System.out.print("Enter Time (HH:MM, 24-hour format): ");
                        LocalTime time = LocalTime.parse(scanner.nextLine().trim());

                        service.bookAppointment(
                                appointmentId, pId, dId, date, time);
                        break;

                    case 4:
                        System.out.print("Enter Doctor ID: ");
                        int availableDoctorId =
                                Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        LocalDate availableDate =
                                LocalDate.parse(scanner.nextLine().trim());

                        service.viewAvailableSlots(
                                availableDoctorId, availableDate);
                        break;

                    case 5:
                        System.out.print("Enter Doctor ID: ");
                        int scheduleDoctorId =
                                Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        LocalDate scheduleDate =
                                LocalDate.parse(scanner.nextLine().trim());

                        service.viewDoctorSchedule(
                                scheduleDoctorId, scheduleDate);
                        break;

                    case 6:
                        System.out.print("Enter Appointment ID: ");
                        int cancelId =
                                Integer.parseInt(scanner.nextLine().trim());

                        service.cancelAppointment(cancelId);
                        break;

                    case 7:
                        System.out.print("Enter Patient ID: ");
                        int historyPatientId =
                                Integer.parseInt(scanner.nextLine().trim());

                        service.viewPatientHistory(historyPatientId);
                        break;

                    case 8:
                        service.displayPatients();
                        break;

                    case 9:
                        service.displayDoctors();
                        break;

                    case 10:
                        System.out.println("Exiting application...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (PatientNotFoundException
                    | DoctorNotFoundException
                    | SlotAlreadyBookedException
                    | InvalidAppointmentSlotException
                    | ValidationException e) {

                System.out.println("Error: " + e.getMessage());

            } catch (java.time.format.DateTimeParseException e) {
                System.out.println(
                        "Invalid date or time format. Please try again.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");

            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}