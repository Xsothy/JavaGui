package Components;

import Controller.ExpenseController;
import Controller.StaffController;
import Model.Expense;
import Model.Staff;
import Support.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A panel for creating and editing expenses.
 */
public class ExpenseFormPanel extends JPanel {
    private final ExpenseController expenseController;
    private final StaffController staffController;
    private final JComponent parentComponent;
    private int expenseId = 0;
    private boolean isEditMode;
    private String selectedImagePath;
    
    // Form components
    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtAmount;
    private JComboBox<ComboItem> cmbStaff;
    private JButton btnBrowse;
    private JLabel lblImage;
    private JButton btnSave;
    private JButton btnCancel;
    
    /**
     * Creates a new ExpenseFormPanel in add mode.
     * 
     * @param parentComponent The parent component for refreshing data
     */
    public ExpenseFormPanel(JComponent parentComponent) {
        this.expenseController = new ExpenseController();
        this.staffController = new StaffController();
        this.parentComponent = parentComponent;
        this.isEditMode = false;
        
        initComponents();
        setupListeners();
        loadStaffComboBox();
    }
    
    /**
     * Creates a new ExpenseFormPanel in edit mode.
     * 
     * @param expenseId The ID of the expense to edit
     * @param expense The expense object
     * @param parentComponent The parent component for refreshing data
     */
    public ExpenseFormPanel(int expenseId, Expense expense, JComponent parentComponent) {
        this.expenseController = new ExpenseController();
        this.staffController = new StaffController();
        this.parentComponent = parentComponent;
        this.expenseId = expenseId;
        this.isEditMode = true;
        this.selectedImagePath = expense.getPicture();
        
        initComponents();
        setupListeners();
        loadStaffComboBox();
        
        // Fill the form with the expense data
        txtName.setText(expense.getName());
        txtDescription.setText(expense.getDescription());
        txtAmount.setText(expense.getAmount().toString());
        
        // Set the selected staff
        for (int i = 0; i < cmbStaff.getItemCount(); i++) {
            ComboItem item = cmbStaff.getItemAt(i);
            if (item.getId() == expense.getStaffId()) {
                cmbStaff.setSelectedItem(item);
                break;
            }
        }
        
        // Display the image if available
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            displayImage(selectedImagePath);
        }
    }
    
    /**
     * Initialize the components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(600, 500));
        
        // Form title
        JLabel titleLabel = new JLabel(isEditMode ? "Edit Expense" : "Add New Expense");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        add(titleLabel, BorderLayout.NORTH);
        
        // Main panel with form and image
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(new Color(255, 255, 255));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        addFormField(formPanel, "Name:", txtName = new JTextField(20), 0, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(descLabel, gbc);
        
        txtDescription = new JTextArea(5, 20);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(scrollPane, gbc);
        
        addFormField(formPanel, "Amount:", txtAmount = new JTextField(20), 2, gbc);
        
        JLabel staffLabel = new JLabel("Staff:");
        staffLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(staffLabel, gbc);
        
        cmbStaff = new JComboBox<>();
        cmbStaff.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(cmbStaff, gbc);
        
        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout(0, 10));
        imagePanel.setBackground(new Color(255, 255, 255));
        imagePanel.setBorder(BorderFactory.createTitledBorder("Image"));
        
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(200, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        btnBrowse = new JButton("Browse");
        btnBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        imagePanel.add(lblImage, BorderLayout.CENTER);
        imagePanel.add(btnBrowse, BorderLayout.SOUTH);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(imagePanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));
        
        btnSave = new JButton(isEditMode ? "Update" : "Save");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(57, 117, 247));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Add a form field to the panel.
     * 
     * @param panel The panel to add to
     * @param labelText The label text
     * @param field The field component
     * @param row The row index
     * @param gbc The GridBagConstraints
     */
    private void addFormField(JPanel panel, String labelText, JComponent field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(field, gbc);
    }
    
    /**
     * Load staff members into the combo box.
     */
    private void loadStaffComboBox() {
        try {
            List<Staff> staffList = staffController.getAllStaff();
            for (Staff staff : staffList) {
                cmbStaff.addItem(new ComboItem(staff.getId(), staff.getName()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExpenseFormPanel.class.getName()).log(Level.SEVERE, "Error loading staff data", ex);
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Setup component listeners.
     */
    private void setupListeners() {
        // Browse button listener
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath = selectedFile.getAbsolutePath();
                displayImage(selectedImagePath);
            }
        });
        
        // Save button listener
        btnSave.addActionListener(e -> {
            try {
                if (validateForm()) {
                    if (isEditMode) {
                        updateExpense();
                    } else {
                        createExpense();
                    }
                    
                    // Close the window
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window != null) {
                        window.dispose();
                    }
                }
            } catch (SQLException | IOException ex) {
                Logger.getLogger(ExpenseFormPanel.class.getName()).log(Level.SEVERE, "Error saving expense", ex);
                JOptionPane.showMessageDialog(this, "Error saving expense: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Cancel button listener
        btnCancel.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });
    }
    
    /**
     * Display an image in the image label.
     * 
     * @param imagePath The path to the image
     */
    private void displayImage(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            Logger.getLogger(ExpenseFormPanel.class.getName()).log(Level.WARNING, "Error displaying image", ex);
            lblImage.setIcon(null);
            lblImage.setText("Error loading image");
        }
    }
    
    /**
     * Validate the form inputs.
     * 
     * @return true if the form is valid, false otherwise
     */
    private boolean validateForm() {
        // Check name
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        
        // Check description
        if (txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtDescription.requestFocus();
            return false;
        }
        
        // Check amount
        try {
            BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                txtAmount.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtAmount.requestFocus();
            return false;
        }
        
        // Check staff
        if (cmbStaff.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a staff member",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            cmbStaff.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Create a new expense.
     * 
     * @throws SQLException If a database error occurs
     * @throws IOException If an error occurs while handling the image file
     */
    private void createExpense() throws SQLException, IOException {
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();
        BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
        int staffId = ((ComboItem) cmbStaff.getSelectedItem()).getId();
        
        try {
            Expense expense = expenseController.createExpense(name, description, amount, selectedImagePath, staffId);
            JOptionPane.showMessageDialog(this, "Expense created successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
    
    /**
     * Update an existing expense.
     * 
     * @throws SQLException If a database error occurs
     * @throws IOException If an error occurs while handling the image file
     */
    private void updateExpense() throws SQLException, IOException {
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();
        BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
        int staffId = ((ComboItem) cmbStaff.getSelectedItem()).getId();
        
        try {
            // Get the expense from the database
            Expense expense = expenseController.getExpenseById(expenseId)
                    .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
            
            // Update the expense
            expense.setName(name);
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setStaffId(staffId);
            
            // If the image was changed, update it
            String newImagePath = selectedImagePath;
            if (selectedImagePath != null && !selectedImagePath.equals(expense.getPicture())) {
                newImagePath = selectedImagePath;
            }
            
            boolean success = expenseController.updateExpense(expense, newImagePath);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Expense updated successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update expense",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
    
    /**
     * A class to represent an item in a combo box with an ID and display text.
     */
    private static class ComboItem {
        private final int id;
        private final String text;
        
        public ComboItem(int id, String text) {
            this.id = id;
            this.text = text;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return text;
        }
    }
} 