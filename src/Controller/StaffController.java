package Controller;

import Model.Staff;
import Repository.StaffRepository;
import Support.Response;

import java.util.List;
import java.util.Optional;

public class StaffController {
    private final StaffRepository staffRepository;

    public StaffController() {
        this.staffRepository = new StaffRepository();
    }

    public Response<List<Staff>> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    public Response<Optional<Staff>> getStaffById(int id) {
        return staffRepository.getStaffById(id);
    }

    public Response<Optional<Staff>> getStaffByUserName(String userName) {
        return staffRepository.getStaffByUserName(userName);
    }

    public Response<Staff> createStaff(String name, String position, String userName, String password, String role) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            return Response.error("Staff name cannot be empty");
        }
        if (userName == null || userName.trim().isEmpty()) {
            return Response.error("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            return Response.error("Password cannot be empty");
        }

        // Check if username already exists
        Response<Boolean> existsResponse = staffRepository.checkStaffExists(userName);
        if (!existsResponse.isSuccess()) {
            return Response.error("Error checking username: " + existsResponse.getMessage());
        }
        if (existsResponse.getData()) {
            return Response.error("Username already exists");
        }

        // Default role if not provided
        if (role == null || role.trim().isEmpty()) {
            role = "staff";
        }

        return staffRepository.createStaff(name, position, userName, password, role);
    }

    public Response<Boolean> updateStaff(Staff staff) {
        // Validate input
        if (staff == null) {
            return Response.error("Staff cannot be null");
        }
        if (staff.getId() <= 0) {
            return Response.error("Invalid staff ID");
        }
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            return Response.error("Staff name cannot be empty");
        }
        if (staff.getUserName() == null || staff.getUserName().trim().isEmpty()) {
            return Response.error("Username cannot be empty");
        }

        // Check if staff exists
        Response<Optional<Staff>> existingStaffResponse = staffRepository.getStaffById(staff.getId());
        if (!existingStaffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + existingStaffResponse.getMessage());
        }
        if (existingStaffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        // Check if the username is changed and already exists
        Staff existingStaff = existingStaffResponse.getData().get();
        if (!existingStaff.getUserName().equals(staff.getUserName())) {
            Response<Boolean> existsResponse = staffRepository.checkStaffExists(staff.getUserName());
            if (existsResponse.isSuccess() && existsResponse.getData()) {
                return Response.error("Username already exists");
            }
        }

        return staffRepository.updateStaff(staff);
    }

    public Response<Boolean> updateStaffWithPassword(Staff staff, String newPassword) {
        // Validate input
        if (staff == null) {
            return Response.error("Staff cannot be null");
        }
        if (staff.getId() <= 0) {
            return Response.error("Invalid staff ID");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Response.error("Password cannot be empty");
        }

        // Check if staff exists
        Response<Optional<Staff>> existingStaffResponse = staffRepository.getStaffById(staff.getId());
        if (!existingStaffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + existingStaffResponse.getMessage());
        }
        if (existingStaffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        return staffRepository.updateStaffWithPassword(staff, newPassword);
    }

    public Response<Boolean> deleteStaff(int id) {
        // Validate input
        if (id <= 0) {
            return Response.error("Invalid staff ID");
        }

        // Check if staff exists
        Response<Optional<Staff>> existingStaffResponse = staffRepository.getStaffById(id);
        if (!existingStaffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + existingStaffResponse.getMessage());
        }
        if (existingStaffResponse.getData().isEmpty()) {
            return Response.error("Staff not found");
        }

        return staffRepository.deleteStaff(id);
    }

    public Response<List<Staff>> getStaffByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return Response.error("Role cannot be empty");
        }
        return staffRepository.getStaffByRole(role);
    }

    public Response<Boolean> validateLogin(String userName, String password) {
        if (userName == null || userName.trim().isEmpty()) {
            return Response.error("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            return Response.error("Password cannot be empty");
        }

        Response<Optional<Staff>> staffResponse = staffRepository.getStaffByUserName(userName);
        if (!staffResponse.isSuccess()) {
            return Response.error("Error accessing staff data: " + staffResponse.getMessage());
        }

        Optional<Staff> staffOpt = staffResponse.getData();
        if (staffOpt.isEmpty()) {
            return Response.error("Invalid username or password");
        }

        Staff staff = staffOpt.get();
        String hashedPassword = Staff.hashPassword(password);

        if (!hashedPassword.equals(staff.getPassword())) {
            return Response.error("Invalid username or password");
        }

        return Response.success("Login successful", true);
    }
}