package com.agree_application;

import java.sql.*;
import java.util.*;
//its my own idea project ----lets start.
public class FarmerBrokerSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/farm_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sanjaysri@123";

    private static Connection conn;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            mainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mainMenu() throws SQLException {
        while (true) {
            System.out.println("\n MAIN MENU:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void register() throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter mobile number: ");
        String mobile = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Select user type:");
        System.out.println("1. Farmer");
        System.out.println("2. Broker");
        System.out.println("3. Harvester");
        System.out.print("Choice: ");
        int userTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String userType;
        switch (userTypeChoice) {
            case 1:
                userType = "Farmer";
                break;
            case 2:
                userType = "Broker";
                break;
            case 3:
                userType = "Harvester";
                break;
            default:
                System.out.println("Invalid choice. Registration cancelled.");
                return;
        }

        String sql = "INSERT INTO users (name, mobile, email, password, user_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, mobile);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, userType);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                switch (userType) {
                    case "Farmer":
                        registerFarmerProfile(userId);
                        break;
                    case "Broker":
                        registerBrokerProfile(userId);
                        break;
                    case "Harvester":
                        registerHarvesterProfile(userId);
                        break;
                }
            }
        }
        System.out.println("Registration successful!");
    }

    private static void registerFarmerProfile(int userId) throws SQLException {
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter district: ");
        String district = scanner.nextLine();
        System.out.print("Enter state: ");
        String state = scanner.nextLine();
        System.out.print("Enter land area (in acres): ");
        double landArea = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter crop type: ");
        String cropType = scanner.nextLine();

        String sql = "INSERT INTO farmer_profiles (user_id, address, district, state, land_area, crop_type, payment_due) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, address);
            pstmt.setString(3, district);
            pstmt.setString(4, state);
            pstmt.setDouble(5, landArea);
            pstmt.setString(6, cropType);
            pstmt.setDouble(7, 0.0); // Initialize payment_due to 0
            pstmt.executeUpdate();
        }
    }

    private static void registerBrokerProfile(int userId) throws SQLException {
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter district: ");
        String district = scanner.nextLine();
        System.out.print("Enter state: ");
        String state = scanner.nextLine();

        String sql = "INSERT INTO broker_profiles (user_id, address, district, state) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, address);
            pstmt.setString(3, district);
            pstmt.setString(4, state);
            pstmt.executeUpdate();
        }
    }

    private static void registerHarvesterProfile(int userId) throws SQLException {
        System.out.print("Enter gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter district: ");
        String district = scanner.nextLine();
        System.out.print("Enter state: ");
        String state = scanner.nextLine();
        System.out.println("Select harvester machine type:");
        System.out.println("1. Kubota");
        System.out.println("2. Preet");
        System.out.println("3. New Holland");
        System.out.println("4. John Deere");
        System.out.println("5. Kartar");
        System.out.println("6. Mahindra HarvestMaster");
        System.out.println("7. Malkit");
        System.out.print("Choice: ");
        int machineChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter number of machines: ");
        int numMachines = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "INSERT INTO harvester_profiles (user_id, gender, address, district, state, machine_type, num_machines) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, gender);
            pstmt.setString(3, address);
            pstmt.setString(4, district);
            pstmt.setString(5, state);
            pstmt.setString(6, getMachineType(machineChoice));
            pstmt.setInt(7, numMachines);
            pstmt.executeUpdate();
        }
    }

    private static void login() throws SQLException {
        System.out.print("Enter mobile number or email: ");
        String identifier = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM users WHERE (mobile = ? OR email = ?) AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                System.out.println("Welcome, " + rs.getString("name") + "!");
                int userId = rs.getInt("id");
                String userType = rs.getString("user_type");
                switch (userType) {
                    case "Farmer":
                        farmerMenu(userId);
                        break;
                    case "Broker":
                        brokerMenu(userId);
                        break;
                    case "Harvester":
                        harvesterMenu(userId);
                        break;
                }
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    private static void farmerMenu(int userId) throws SQLException {
        while (true) {
            System.out.println("\nWelcome, Farmer!");
            double paymentDue = getPaymentDue(userId);
            System.out.println("Payment to be paid to the broker: " + paymentDue);

            System.out.println("1. Make Payment");
            System.out.println("2. View Profile");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    if (paymentDue > 0) {
                        makePayment(userId, paymentDue);
                    } else {
                        System.out.println("No payment due.");
                    }
                    break;
                case 2:
                    viewFarmerProfile(userId);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static double getPaymentDue(int userId) throws SQLException {
        String sql = "SELECT payment_due FROM farmer_profiles WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("payment_due");
            }
        }
        return 0;
    }

    private static void makePayment(int userId, double paymentDue) throws SQLException {
        System.out.println("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 

        if (amount > paymentDue) {
            System.out.println("Payment amount exceeds the due amount. Please enter a valid amount.");
            return;
        }

        System.out.println("Select payment method:");
        System.out.println("1. GPay");
        System.out.println("2. PhonePe");
        System.out.println("3. UPI");
        System.out.println("4. Debit Card");
        System.out.println("5. Mobile Banking");
        System.out.print("Choice: ");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "UPDATE farmer_profiles SET payment_due = payment_due - ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Payment sent successfully via " + getPaymentMethodName(paymentMethod) + "!");
            } else {
                System.out.println("Failed to process payment. Please try again.");
            }
        }
    }

    private static void viewFarmerProfile(int userId) throws SQLException {
        String sql = "SELECT * FROM farmer_profiles WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nFarmer Profile:");
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("District: " + rs.getString("district"));
                System.out.println("State: " + rs.getString("state"));
                System.out.println("Land Area: " + rs.getDouble("land_area") + " acres");
                System.out.println("Crop Type: " + rs.getString("crop_type"));
                System.out.println("Payment Due: $" + rs.getDouble("payment_due"));
            } else {
                System.out.println("Profile not found.");
            }
        }
    }

    private static void brokerMenu(int userId) throws SQLException {
        while (true) {
            System.out.println("\nWelcome, Broker!");
            System.out.println("Choose an option:");
            System.out.println("1. Update Farmer Payment");
            System.out.println("2. Request Harvester");
            System.out.println("3. View Profile");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    updateFarmerPayment();
                    break;
                case 2:
                    requestHarvester(userId);
                    break;
                case 3:
                    viewBrokerProfile(userId);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void updateFarmerPayment() throws SQLException {
        System.out.print("Enter farmer's mobile number or email: ");
        String farmerIdentifier = scanner.nextLine();

        int farmerId = getFarmerId(farmerIdentifier);
        if (farmerId == -1) {
            System.out.println("Farmer not found.");
            return;
        }

        System.out.print("Enter payment amount for Farmer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 

        String sql = "UPDATE farmer_profiles SET payment_due = payment_due + ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, farmerId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Payment updated successfully!");
            } else {
                System.out.println("Failed to update payment. Please try again.");
            }
        }
    }

    private static int getFarmerId(String identifier) throws SQLException {
        String sql = "SELECT id FROM users WHERE (mobile = ? OR email = ?) AND user_type = 'Farmer'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private static void requestHarvester(int brokerId) throws SQLException {
        System.out.println("Select wheat harvester machine type:");
        System.out.println("1. Kubota");
        System.out.println("2. Preet");
        System.out.println("3. New Holland");
        System.out.println("4. John Deere");
        System.out.println("5. Kartar");
        System.out.println("6. Mahindra HarvestMaster");
        System.out.println("7. Malkit");
        System.out.print("Choice: ");
        int machineChoice = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter state: ");
        String state = scanner.nextLine();
        System.out.print("Enter district: ");
        String district = scanner.nextLine();

        List<HarvesterInfo> availableHarvesters = getAvailableHarvesters(state, district, getMachineType(machineChoice));

        if (availableHarvesters.isEmpty()) {
            System.out.println("No harvesters are available in the specified location with the selected machine type.");
            return;
        }

        System.out.println("\nAvailable Harvesters:");
        for (int i = 0; i < availableHarvesters.size(); i++) {
            HarvesterInfo harvester = availableHarvesters.get(i);
            System.out.println((i + 1) + ". Name: " + harvester.name + ", Mobile: " + harvester.mobile);
        }

        System.out.print("Select a harvester (enter the number): ");
        int harvesterChoice = scanner.nextInt();
        scanner.nextLine(); 

        if (harvesterChoice < 1 || harvesterChoice > availableHarvesters.size()) {
            System.out.println("Invalid choice. Request cancelled.");
            return;
        }

        HarvesterInfo selectedHarvester = availableHarvesters.get(harvesterChoice - 1);

        System.out.print("Enter payment per hour: ");
        double paymentPerHour = scanner.nextDouble();
        scanner.nextLine(); 

        String sql = "INSERT INTO harvester_requests (broker_id, harvester_id, payment_per_hour, state, district, machine_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, brokerId);
            pstmt.setInt(2, selectedHarvester.id);
            pstmt.setDouble(3, paymentPerHour);
            pstmt.setString(4, state);
            pstmt.setString(5, district);
            pstmt.setString(6, getMachineType(machineChoice));
            pstmt.executeUpdate();
        }

        System.out.println("Request sent successfully to " + selectedHarvester.name + "!");
    }

    private static List<HarvesterInfo> getAvailableHarvesters(String state, String district, String machineType) throws SQLException {
        List<HarvesterInfo> harvesters = new ArrayList<>();
        String sql = "SELECT u.id, u.name, u.mobile FROM users u " +
                     "JOIN harvester_profiles hp ON u.id = hp.user_id " +
                     "WHERE hp.state = ? AND hp.district = ? AND hp.machine_type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, state);
            pstmt.setString(2, district);
            pstmt.setString(3, machineType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                harvesters.add(new HarvesterInfo(rs.getInt("id"), rs.getString("name"), rs.getString("mobile")));
            }
        }
        return harvesters;
    }

    private static void viewBrokerProfile(int userId) throws SQLException {
        String sql = "SELECT * FROM broker_profiles WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nBroker Profile:");
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("District: " + rs.getString("district"));
                System.out.println("State: " + rs.getString("state"));
            } else {
                System.out.println("Profile not found.");
            }
        }
    }

    private static void harvesterMenu(int userId) throws SQLException {
        while (true) {
            System.out.println("\nWelcome, Harvester!");
            System.out.println("Choose an option:");
            System.out.println("1. Update Profile");
            System.out.println("2. Check Requests");
            System.out.println("3. View Profile");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    updateHarvesterProfile(userId);
                    break;
                case 2:
                    checkHarvesterRequests(userId);
                    break;
                case 3:
                    viewHarvesterProfile(userId);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void updateHarvesterProfile(int userId) throws SQLException {
        System.out.print("Enter gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter district: ");
        String district = scanner.nextLine();
        System.out.print("Enter state: ");
        String state = scanner.nextLine();

        System.out.println("Select harvester machine type:");
        System.out.println("1. Kubota");
        System.out.println("2. Preet");
        System.out.println("3. New Holland");
        System.out.println("4. John Deere");
        System.out.println("5. Kartar");
        System.out.println("6. Mahindra HarvestMaster");
        System.out.println("7. Malkit");
        System.out.print("Choice: ");
        int machineChoice = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter number of machines: ");
        int numMachines = scanner.nextInt();
        scanner.nextLine(); 
        String sql = "UPDATE harvester_profiles SET gender = ?, address = ?, district = ?, state = ?, machine_type = ?, num_machines = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gender);
            pstmt.setString(2, address);
            pstmt.setString(3, district);
            pstmt.setString(4, state);
            pstmt.setString(5, getMachineType(machineChoice));
            pstmt.setInt(6, numMachines);
            pstmt.setInt(7, userId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Failed to update profile. Please try again.");
            }
        }
    }

    private static void checkHarvesterRequests(int userId) throws SQLException {
        String sql = "SELECT hr.*, u.name as broker_name, u.mobile as broker_mobile, bp.address as broker_address, bp.district as broker_district, bp.state as broker_state " +
                     "FROM harvester_requests hr " +
                     "JOIN users u ON hr.broker_id = u.id " +
                     "JOIN broker_profiles bp ON u.id = bp.user_id " +
                     "WHERE hr.harvester_id = ? AND hr.status = 'Pending'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("\nRequest " + count + ":");
                System.out.println("Broker: " + rs.getString("broker_name"));
                System.out.println("Mobile Number: " + rs.getString("broker_mobile"));
                System.out.println("Address: " + rs.getString("broker_address"));
                System.out.println("District: " + rs.getString("broker_district"));
                System.out.println("State: " + rs.getString("broker_state"));
                System.out.println("Payment per Hour: $" + rs.getDouble("payment_per_hour"));
                System.out.println("State: " + rs.getString("state"));
                System.out.println("District: " + rs.getString("district"));
                System.out.println("Machine Type: " + rs.getString("machine_type"));
            }

            if (count == 0) {
                System.out.println("No pending requests.");
            } else {
                System.out.println("\nYou have " + count + " request(s) from brokers.");
                System.out.println("1. Accept a request");
                System.out.println("2. Reject a request");
                System.out.println("3. Go back");
                System.out.print("Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                if (choice == 1 || choice == 2) {
                    System.out.print("Enter the request number to " + (choice == 1 ? "accept" : "reject") + ": ");
                    int requestNumber = scanner.nextInt();
                    scanner.nextLine();

                    if (requestNumber > 0 && requestNumber <= count) {
                        updateRequestStatus(userId, requestNumber, choice == 1 ? "Accepted" : "Rejected");
                    } else {
                        System.out.println("Invalid request number.");
                    }
                }
            }
        }
    }

    private static void updateRequestStatus(int harvesterId, int requestNumber, String status) throws SQLException {
        String sql = "UPDATE harvester_requests SET status = ?, harvester_id = ? " +
                     "WHERE id = (SELECT id FROM (SELECT hr.id, @row_num := @row_num + 1 AS row_num " +
                     "FROM harvester_requests hr, (SELECT @row_num := 0) r " +
                     "WHERE hr.status = 'Pending' ORDER BY hr.id) " +
                     "AS numbered_rows WHERE row_num = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, harvesterId);
            pstmt.setInt(3, requestNumber);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Request " + status.toLowerCase() + " successfully.");
            } else {
                System.out.println("Failed to update request status. Please try again.");
            }
        }
    }

    private static void viewHarvesterProfile(int userId) throws SQLException {
        String sql = "SELECT * FROM harvester_profiles WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nHarvester Profile:");
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("District: " + rs.getString("district"));
                System.out.println("State: " + rs.getString("state"));
                System.out.println("Machine Type: " + rs.getString("machine_type"));
                System.out.println("Number of Machines: " + rs.getInt("num_machines"));
            } else {
                System.out.println("Profile not found.");
            }
        }
    }

    private static String getMachineType(int choice) {
        switch (choice) {
            case 1:
                return "Kubota";
            case 2:
                return "Preet";
            case 3:
                return "New Holland";
            case 4:
                return "John Deere";
            case 5:
                return "Kartar";
            case 6:
                return "Mahindra HarvestMaster";
            case 7:
                return "Malkit";
            default:
                return "mechine type not specified.......";
        }
    }

    private static String getPaymentMethodName(int choice) {
        switch (choice) {
            case 1:
                return "GPay";
            case 2:
                return "PhonePe";
            case 3:
                return "UPI";
            case 4:
                return "Debit Card";
            case 5:
                return "Mobile Banking";
            default:
                return "Unknown Method";
        }
    }

    private static class HarvesterInfo {
        int id;
        String name;
        String mobile;

        HarvesterInfo(int id, String name, String mobile) {
            this.id = id;
            this.name = name;
            this.mobile = mobile;
        }
    }
}

