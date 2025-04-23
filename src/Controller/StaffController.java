package Controller;

import Exception.ValidateException;
import Model.Staff;
import Model.StaffWithExpenses;
import Repository.StaffRepository;
import Support.SessionManager;
import View.*;
import View.Dashboard.StaffDetailsPanel;
import View.Dashboard.StaffFormPanel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;

public class StaffController {
    private final StaffRepository staffRepository;

    public StaffController() {
        this.staffRepository = new StaffRepository();
    }

    public NavigatePanel show(Map<String, String> parameters) {
        int staffId = Integer.parseInt(parameters.get("id"));

        Optional<Staff> staffOpt = staffRepository.getStaffById(staffId);
        if (staffOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Staff not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }

        return new StaffDetailsPanel(staffOpt.get());
    }

    public NavigatePanel edit(Map<String, String> parameters) {
        int staffId = Integer.parseInt(parameters.get("id"));

        Optional<Staff> staffOpt = staffRepository.getStaffById(staffId);
        if (staffOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Staff not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }

        return new StaffFormPanel(staffOpt.get());
    }

    public List<Staff> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    public Optional<Staff> getStaffById(int id) {
        return staffRepository.getStaffById(id);
    }

    public Optional<Staff> getStaffByUserName(String userName) {
        return staffRepository.getStaffByUserName(userName);
    }

    public Staff createStaff(
            String name, String position, String userName, String password, String role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty");
        }
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (staffRepository.checkStaffExists(userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (role == null || role.trim().isEmpty()) {
            role = "staff";
        }

        String hashedPassword = Staff.hashPassword(password);

        return staffRepository.createStaff(name, position, userName, hashedPassword, role);
    }

    public boolean updateStaff(Staff staff) {
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

        Optional<Staff> existingStaffOpt = staffRepository.getStaffById(staff.getId());
        if (existingStaffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        Staff existingStaff = existingStaffOpt.get();
        if (!existingStaff.getUserName().equals(staff.getUserName())) {
            if (staffRepository.checkStaffExists(staff.getUserName())) {
                throw new IllegalArgumentException("Username already exists");
            }
        }

        return staffRepository.updateStaff(staff);
    }

    public boolean updateStaffWithPassword(Staff staff, String newPassword) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (staff.getId() <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Optional<Staff> existingStaffOpt = staffRepository.getStaffById(staff.getId());
        if (existingStaffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        String hashedPassword = Staff.hashPassword(newPassword);

        return staffRepository.updateStaffWithPassword(staff, hashedPassword);
    }

    public boolean deleteStaff(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }

        Optional<Staff> existingStaffOpt = staffRepository.getStaffById(id);
        if (existingStaffOpt.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }

        return staffRepository.deleteStaff(id);
    }

    public List<Staff> getStaffByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        return staffRepository.getStaffByRole(role);
    }

    public void login(String userName, String password) throws ValidateException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new ValidateException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidateException("Password cannot be empty");
        }

        Optional<Staff> staffOpt = staffRepository.getStaffByUserName(userName);
        if (staffOpt.isEmpty()) {
            throw new ValidateException("Invalid username or password");
        }

        Staff staff = staffOpt.get();
//        String hashedPassword = Staff.hashPassword(password);

        if (!password.equals(staff.getPassword())) {
            throw new ValidateException("Invalid username or password");
        }

        SessionManager.setCurrentUser(staff);
    }



    public Optional<StaffWithExpenses> getStaffWithExpensesById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        return staffRepository.getStaffWithExpensesById(id);
    }

    public List<StaffWithExpenses> getAllStaffWithExpenses() {
        return staffRepository.getAllStaffWithExpenses();
    }
}
