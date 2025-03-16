package Controller;

import Model.Staff;
import Repository.StaffRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing staff operations.
 * Provides business logic and validation for staff-related operations.
 */
public class StaffController {
    private final StaffRepository staffRepository;

    /**
     * Creates a new StaffController.
     */
    public StaffController() {
        this.staffRepository = new StaffRepository();
    }

    /**
     * Gets all staff members.
     * 
     * @return A list of all staff members
     * @throws SQLException If a database error occurs
     */
    public List<Staff> getAllStaff() throws SQLException {
        return staffRepository.getAllStaff();
    }

    /**
     * Gets a staff member by ID.
     * 
     * @param id The ID of the staff member
     * @return An Optional containing the staff member if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<Staff> getStaffById(int id) throws SQLException {
        return staffRepository.getStaffById(id);
    }

    /**
     * Gets a staff member by username.
     * 
     * @param userName The username of the staff member
     * @return An Optional containing the staff member if found, or empty if not found
     * @throws SQLException If a database error occurs
     */
    public Optional<Staff> getStaffByUserName(String userName) throws SQLException {
        return staffRepository.getStaffByUserName(userName);
    }

    /**
     * Creates a new staff member.
     * 
     * @param name The name of the staff member
     * @param position The position of the staff member
     * @param userName The username of the staff member
     * @param password The password of the staff member
     * @param role The role of the staff member
     * @return The created staff member
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public Staff createStaff(String name, String position, String userName, String password, String role) 
            throws IllegalArgumentException, SQLException {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty");
        }
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if username already exists
        boolean exists = staffRepository.checkStaffExists(userName);
        if (exists) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Default role if not provided
        if (role == null || role.trim().isEmpty()) {
            role = "staff";
        }

        // Hash the password before storing
        String hashedPassword = Staff.hashPassword(password);
        
        return staffRepository.createStaff(name, position, userName, hashedPassword, role);
    }

    /**
     * Updates a staff member.
     * 
     * @param staff The staff member to update
     * @return true if the update was successful, false otherwise
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public boolean updateStaff(Staff staff) throws IllegalArgumentException, SQLException {
        // Validate input
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (staff.getId() <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty");
        }
        if (staff.getUserName() == null || staff.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        // Check if staff exists
        Optional<Staff> existingStaffOpt = staffRepository.getStaffById(staff.getId());
        if (existingStaffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        // Check if the username is changed and already exists
        Staff existingStaff = existingStaffOpt.get();
        if (!existingStaff.getUserName().equals(staff.getUserName())) {
            boolean exists = staffRepository.checkStaffExists(staff.getUserName());
            if (exists) {
                throw new IllegalArgumentException("Username already exists");
            }
        }

        return staffRepository.updateStaff(staff);
    }

    /**
     * Updates a staff member with a new password.
     * 
     * @param staff The staff member to update
     * @param newPassword The new password
     * @return true if the update was successful, false otherwise
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public boolean updateStaffWithPassword(Staff staff, String newPassword) 
            throws IllegalArgumentException, SQLException {
        // Validate input
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (staff.getId() <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if staff exists
        Optional<Staff> existingStaffOpt = staffRepository.getStaffById(staff.getId());
        if (existingStaffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        // Hash the password before storing
        String hashedPassword = Staff.hashPassword(newPassword);
        
        return staffRepository.updateStaffWithPassword(staff, hashedPassword);
    }

    /**
     * Deletes a staff member.
     * 
     * @param id The ID of the staff member to delete
     * @return true if the deletion was successful, false otherwise
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public boolean deleteStaff(int id) throws IllegalArgumentException, SQLException {
        // Validate input
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }

        // Check if staff exists
        Optional<Staff> existingStaffOpt = staffRepository.getStaffById(id);
        if (existingStaffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        return staffRepository.deleteStaff(id);
    }

    /**
     * Gets staff members by role.
     * 
     * @param role The role to filter by
     * @return A list of staff members with the specified role
     * @throws IllegalArgumentException If the input is invalid
     * @throws SQLException If a database error occurs
     */
    public List<Staff> getStaffByRole(String role) throws IllegalArgumentException, SQLException {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        return staffRepository.getStaffByRole(role);
    }

    /**
     * Validates a login attempt.
     * 
     * @param userName The username
     * @param password The password
     * @throws IllegalArgumentException If the login is invalid
     * @throws SQLException If a database error occurs
     */
    public void validateLogin(String userName, String password) 
            throws IllegalArgumentException, SQLException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Optional<Staff> staffOpt = staffRepository.getStaffByUserName(userName);
        if (staffOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        Staff staff = staffOpt.get();
        String hashedPassword = Staff.hashPassword(password);

        if (!hashedPassword.equals(staff.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        
        // Login successful, no exception thrown
    }
}