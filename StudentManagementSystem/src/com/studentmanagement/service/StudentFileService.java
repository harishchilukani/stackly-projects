package com.studentmanagement.service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.studentmanagement.model.Course;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.StudentStatus;

public class StudentFileService {

    private final Path filePath = Path.of("students.txt");

    // Save students to file
    public void saveStudents(List<Student> students) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {

            for (Student student : students) {

                String data = student.getId() + "|" +
                        student.getName() + "|" +
                        student.getEmail() + "|" +
                        student.getMarks() + "|" +
                        student.getAttendance() + "|" +
                        student.getStatus() + "|" +
                        student.getCourse().getCourseId() + "|" +
                        student.getCourse().getCourseName() + "|" +
                        student.getCourse().getDuration();

                writer.write(data);
                writer.newLine();
            }
        }

        System.out.println("Student records saved successfully");
    }

    // Load students from file
    public List<Student> loadStudents() throws IOException {

        List<Student> students = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return students;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 9) {
                    throw new IOException("Invalid student record: " + line);
                }

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String email = data[2];
                double marks = Double.parseDouble(data[3]);
                int attendance = Integer.parseInt(data[4]);
                StudentStatus status = StudentStatus.valueOf(data[5]);

                int courseId = Integer.parseInt(data[6]);
                String courseName = data[7];
                int duration = Integer.parseInt(data[8]);

                Course course = new Course(courseId, courseName, duration);

                Student student = new Student(
                        id, name, email, marks,
                        attendance, status, course
                );

                students.add(student);
            }
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid data in student file", e);
        }

        System.out.println("Student records loaded successfully");

        return students;
    }
}

