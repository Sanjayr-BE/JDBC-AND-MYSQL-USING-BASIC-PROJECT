package com.HMS;

import java.sql.*;
import java.util.Scanner;

public class HealthcareManagementSystem {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Healthcare_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "*******";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n1. Add Patient\n2. Add Doctor\n3. Schedule Appointment\n4. Add Medical Record\n5. Issue Prescription\n6. View Appointments\n7. View Medical Records\n8. View Prescriptions\n9. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        addPatient(connection, scanner);
                        break;
                    case 2:
                        addDoctor(connection, scanner);
                        break;
                    case 3:
                        scheduleAppointment(connection, scanner);
                        break;
                    case 4:
                        addMedicalRecord(connection, scanner);
                        break;
                    case 5:
                        issuePrescription(connection, scanner);
                        break;
                    case 6:
                        viewAppointments(connection, scanner);
                        break;
                    case 7:
                        viewMedicalRecords(connection, scanner);
                        break;
                    case 8:
                        viewPrescriptions(connection, scanner);
                        break;
                    case 9:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new patient
    private static void addPatient(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter Gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        String query = "INSERT INTO Patients (FirstName, LastName, DateOfBirth, Gender, PhoneNumber, Email, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setDate(3, Date.valueOf(dateOfBirth));
            statement.setString(4, gender);
            statement.setString(5, phoneNumber);
            statement.setString(6, email);
            statement.setString(7, address);
            statement.executeUpdate();
            System.out.println("Patient added successfully!");
        }
    }

    // Method to add a new doctor
    private static void addDoctor(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();

        String query = "INSERT INTO Doctors (FirstName, LastName, Specialization, PhoneNumber, Email, Department) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, specialization);
            statement.setString(4, phoneNumber);
            statement.setString(5, email);
            statement.setString(6, department);
            statement.executeUpdate();
            System.out.println("Doctor added successfully!");
        }
    }

    // Method to schedule an appointment
    private static void scheduleAppointment(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Appointment Date and Time (YYYY-MM-DD HH:MM:SS): ");
        String appointmentDate = scanner.nextLine();
        System.out.print("Enter Notes: ");
        String notes = scanner.nextLine();

        String query = "INSERT INTO Appointments (PatientID, DoctorID, AppointmentDate, Notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            statement.setInt(2, doctorId);
            statement.setTimestamp(3, Timestamp.valueOf(appointmentDate));
            statement.setString(4, notes);
            statement.executeUpdate();
            System.out.println("Appointment scheduled successfully!");
        }
    }

    // Method to add a medical record
    private static void addMedicalRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Record Date (YYYY-MM-DD): ");
        String recordDate = scanner.nextLine();
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter Treatment: ");
        String treatment = scanner.nextLine();
        System.out.print("Enter Notes: ");
        String notes = scanner.nextLine();

        String query = "INSERT INTO MedicalRecords (PatientID, DoctorID, RecordDate, Diagnosis, Treatment, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            statement.setInt(2, doctorId);
            statement.setDate(3, Date.valueOf(recordDate));
            statement.setString(4, diagnosis);
            statement.setString(5, treatment);
            statement.setString(6, notes);
            statement.executeUpdate();
            System.out.println("Medical record added successfully!");
        }
    }

    // Method to issue a prescription
    private static void issuePrescription(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Prescription Date (YYYY-MM-DD): ");
        String prescriptionDate = scanner.nextLine();
        System.out.print("Enter Medication: ");
        String medication = scanner.nextLine();
        System.out.print("Enter Dosage: ");
        String dosage = scanner.nextLine();
        System.out.print("Enter Duration: ");
        String duration = scanner.nextLine();
        System.out.print("Enter Notes: ");
        String notes = scanner.nextLine();

        String query = "INSERT INTO Prescriptions (PatientID, DoctorID, PrescriptionDate, Medication, Dosage, Duration, Notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            statement.setInt(2, doctorId);
            statement.setDate(3, Date.valueOf(prescriptionDate));
            statement.setString(4, medication);
            statement.setString(5, dosage);
            statement.setString(6, duration);
            statement.setString(7, notes);
            statement.executeUpdate();
            System.out.println("Prescription issued successfully!");
        }
    }

    // Method to view appointments for a patient
    private static void viewAppointments(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); 
        String query = "SELECT * FROM Appointments WHERE PatientID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nAppointments for Patient ID: " + patientId);
            while (resultSet.next()) {
                System.out.println("Appointment ID: " + resultSet.getInt("AppointmentID"));
                System.out.println("Doctor ID: " + resultSet.getInt("DoctorID"));
                System.out.println("Appointment Date: " + resultSet.getTimestamp("AppointmentDate"));
                System.out.println("Status: " + resultSet.getString("Status"));
                System.out.println("Notes: " + resultSet.getString("Notes"));
                System.out.println("-----------------------------");
            }
        }
    }

    // Method to view medical records for a patient
    private static void viewMedicalRecords(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine(); 

        String query = "SELECT * FROM MedicalRecords WHERE PatientID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nMedical Records for Patient ID: " + patientId);
            while (resultSet.next()) {
                System.out.println("Record ID: " + resultSet.getInt("RecordID"));
                System.out.println("Doctor ID: " + resultSet.getInt("DoctorID"));
                System.out.println("Record Date: " + resultSet.getDate("RecordDate"));
                System.out.println("Diagnosis: " + resultSet.getString("Diagnosis"));
                System.out.println("Treatment: " + resultSet.getString("Treatment"));
                System.out.println("Notes: " + resultSet.getString("Notes"));
                System.out.println("-----------------------------");
            }
        }
    }

    // Method to view prescriptions for a patient
    private static void viewPrescriptions(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        String query = "SELECT * FROM Prescriptions WHERE PatientID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nPrescriptions for Patient ID: " + patientId);
            while (resultSet.next()) {
                System.out.println("Prescription ID: " + resultSet.getInt("PrescriptionID"));
                System.out.println("Doctor ID: " + resultSet.getInt("DoctorID"));
                System.out.println("Prescription Date: " + resultSet.getDate("PrescriptionDate"));
                System.out.println("Medication: " + resultSet.getString("Medication"));
                System.out.println("Dosage: " + resultSet.getString("Dosage"));
                System.out.println("Duration: " + resultSet.getString("Duration"));
                System.out.println("Notes: " + resultSet.getString("Notes"));
                System.out.println("-----------------------------");
            }
        }
    }
}
