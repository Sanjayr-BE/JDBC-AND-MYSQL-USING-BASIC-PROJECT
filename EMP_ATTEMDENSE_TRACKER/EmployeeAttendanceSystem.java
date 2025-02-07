package com.EMP_ATTEMDENSE_DB;
import java.sql.*;
import java.util.Scanner;

public class EmployeeAttendanceSystem {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "******";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n1. Add Employee\n2. Add Attendance Record\n3. Add Leave Request\n4. View Attendance Records\n5. View Leave Requests\n6. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addEmployee(connection, scanner);
                        break;
                    case 2:
                        addAttendanceRecord(connection, scanner);
                        break;
                    case 3:
                        addLeaveRequest(connection, scanner);
                        break;
                    case 4:
                        viewAttendanceRecords(connection, scanner);
                        break;
                    case 5:
                        viewLeaveRequests(connection);
                        break;
                    case 6:
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

    // Method to add a new employee
    private static void addEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        System.out.print("Enter Hire Date (YYYY-MM-DD): ");
        String hireDate = scanner.nextLine();

        String query = "INSERT INTO Employees (FirstName, LastName, Email, Department, Position, HireDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, department);
            statement.setString(5, position);
            statement.setDate(6, Date.valueOf(hireDate));
            statement.executeUpdate();
            System.out.println("Employee added successfully!");
        }
    }

    // Method to add an attendance record
    private static void addAttendanceRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Attendance Date (YYYY-MM-DD): ");
        String attendanceDate = scanner.nextLine();
        System.out.print("Enter Check-In Time (YYYY-MM-DD HH:MM:SS): ");
        String checkInTime = scanner.nextLine();
        System.out.print("Enter Check-Out Time (YYYY-MM-DD HH:MM:SS): ");
        String checkOutTime = scanner.nextLine();
        System.out.print("Enter Hours Worked: ");
        double hoursWorked = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Status (Present/Absent/Late): ");
        String status = scanner.nextLine();

        String query = "INSERT INTO AttendanceRecords (EmployeeID, AttendanceDate, CheckInTime, CheckOutTime, HoursWorked, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setDate(2, Date.valueOf(attendanceDate));
            statement.setTimestamp(3, Timestamp.valueOf(checkInTime));
            statement.setTimestamp(4, Timestamp.valueOf(checkOutTime));
            statement.setDouble(5, hoursWorked);
            statement.setString(6, status);
            statement.executeUpdate();
            System.out.println("Attendance record added successfully!");
        }
    }

    // Method to add a leave request
    private static void addLeaveRequest(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Leave Type (Sick/Vacation/Personal/Other): ");
        String leaveType = scanner.nextLine();
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        System.out.print("Enter Reason: ");
        String reason = scanner.nextLine();

        String query = "INSERT INTO LeaveRequests (EmployeeID, LeaveType, StartDate, EndDate, Reason) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, leaveType);
            statement.setDate(3, Date.valueOf(startDate));
            statement.setDate(4, Date.valueOf(endDate));
            statement.setString(5, reason);
            statement.executeUpdate();
            System.out.println("Leave request added successfully!");
        }
    }

    // Method to view attendance records for an employee
    private static void viewAttendanceRecords(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        String query = "SELECT * FROM AttendanceRecords WHERE EmployeeID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nAttendance Records for Employee ID: " + employeeId);
            while (resultSet.next()) {
                System.out.println("Attendance ID: " + resultSet.getInt("AttendanceID"));
                System.out.println("Date: " + resultSet.getDate("AttendanceDate"));
                System.out.println("Check-In: " + resultSet.getTimestamp("CheckInTime"));
                System.out.println("Check-Out: " + resultSet.getTimestamp("CheckOutTime"));
                System.out.println("Hours Worked: " + resultSet.getDouble("HoursWorked"));
                System.out.println("Status: " + resultSet.getString("Status"));
                System.out.println("-----------------------------");
            }
        }
    }

    // Method to view all leave requests
    private static void viewLeaveRequests(Connection connection) throws SQLException {
        String query = "SELECT * FROM LeaveRequests";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\nAll Leave Requests:");
            while (resultSet.next()) {
                System.out.println("Leave ID: " + resultSet.getInt("LeaveID"));
                System.out.println("Employee ID: " + resultSet.getInt("EmployeeID"));
                System.out.println("Leave Type: " + resultSet.getString("LeaveType"));
                System.out.println("Start Date: " + resultSet.getDate("StartDate"));
                System.out.println("End Date: " + resultSet.getDate("EndDate"));
                System.out.println("Status: " + resultSet.getString("Status"));
                System.out.println("Reason: " + resultSet.getString("Reason"));
                System.out.println("-----------------------------");
            }
        }
    }
}