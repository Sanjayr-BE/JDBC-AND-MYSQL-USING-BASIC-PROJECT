
-- Create the Employees table
CREATE TABLE IF NOT EXISTS Employees (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Department VARCHAR(50),
    Position VARCHAR(50),
    HireDate DATE NOT NULL
);

-- Create the AttendanceRecords table
CREATE TABLE IF NOT EXISTS AttendanceRecords (
    AttendanceID INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeID INT NOT NULL,
    AttendanceDate DATE NOT NULL,
    CheckInTime TIMESTAMP NOT NULL,
    CheckOutTime TIMESTAMP NOT NULL,
    HoursWorked DECIMAL(5, 2) NOT NULL,
    Status ENUM('Present', 'Absent', 'Late') NOT NULL,
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);

-- Create the LeaveRequests table
CREATE TABLE IF NOT EXISTS LeaveRequests (
    LeaveID INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeID INT NOT NULL,
    LeaveType ENUM('Sick', 'Vacation', 'Personal', 'Other') NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Reason TEXT,
    Status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);
