package Components;

import Controller.ExpenseController;
import Controller.StaffController;
import Model.Expense;
import Model.Staff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * A panel for creating and editing expenses.
 */
public class ExpenseFormPanel extends NavigatePanel {
    private final ExpenseController expenseController;
    private final StaffController staffController;
    private final Router router;
    
    private int expenseId = 0;
    private boolean isEditMode;
    private String selectedImagePath;
    
    // Form components
    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtAmount;
    private JComboBox<StaffComboItem> cmbStaff;
    private JButton btnBrowse;
    private JLabel lblImage;
    private JButton btnSave;
    private JButton btnCancel;
    
    /**
     * Creates a new ExpenseFormPanel for adding a new expense.
     * 
     * @param router The router for navigation
     */
    public ExpenseFormPanel(Router router) {
        this.expenseController = new ExpenseController();
        this.staffController = new StaffController();
        this.router = router;
        this.isEditMode = false;
        
        initComponents();
        loadStaffComboBox();
    }
    
    /**
     * Creates a new ExpenseFormPanel for editing an existing expense.
     * 
     * @param router The router for navigation
     * @param expense The expense to edit
     */
    public ExpenseFormPanel(Router router, Expense expense) {
        this.expenseController = new ExpenseController();
        this.staffController = new StaffController();
        this.router = router;
        this.isEditMode = true;
        this.expenseId = expense.getId();
        this.selectedImagePath = expense.getPicture();
        
        initComponents();
        loadStaffComboBox();
        
        // Populate form with expense data
        txtName.setText(expense.getName());
        txtDescription.setText(expense.getDescription());
        txtAmount.setText(String.format("%.2f", expense.getAmount()));
        
        // Set the selected staff
        for (int i = 0; i < cmbStaff.getItemCount(); i++) {
            StaffComboItem item = cmbStaff.getItemAt(i);
            if (item.getId() == expense.getStaffId()) {
                cmbStaff.setSelectedIndex(i);
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
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBackground(Color.WHITE);
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(
                UIConstants.CONTENT_PADDING, 
                UIConstants.CONTENT_PADDING, 
                UIConstants.CONTENT_PADDING, 
                UIConstants.CONTENT_PADDING
            )
        ));
        
        // Title and back button
        JLabel titleLabel = new JLabel(isEditMode ? "Edit Expense" : "Add New Expense");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        
        JButton backButton = new JButton("Back");
        backButton.setFont(UIConstants.BUTTON_FONT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(UIConstants.PRIMARY_COLOR);
        backButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V, 
            UIConstants.BUTTON_PADDING_H, 
            UIConstants.BUTTON_PADDING_V, 
            UIConstants.BUTTON_PADDING_H
        ));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(UIConstants.ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
        
        backButton.addActionListener(e -> router.navigate("/expenses"));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create form panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.CONTENT_PADDING, 
            UIConstants.CONTENT_PADDING, 
            UIConstants.CONTENT_PADDING, 
            UIConstants.CONTENT_PADDING
        ));
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name field
        addFormLabel(fieldsPanel, "Name:", 0);
        txtName = new JTextField(20);
        txtName.setFont(UIConstants.TABLE_CONTENT_FONT);
        txtName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        fieldsPanel.add(txtName, gbc);
        
        // Description field
        addFormLabel(fieldsPanel, "Description:", 1);
        txtDescription = new JTextArea(4, 20);
        txtDescription.setFont(UIConstants.TABLE_CONTENT_FONT);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(txtDescription);
        descScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        fieldsPanel.add(descScrollPane, gbc);
        
        // Amount field
        addFormLabel(fieldsPanel, "Amount:", 2);
        txtAmount = new JTextField(20);
        txtAmount.setFont(UIConstants.TABLE_CONTENT_FONT);
        txtAmount.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        fieldsPanel.add(txtAmount, gbc);
        
        // Staff dropdown
        addFormLabel(fieldsPanel, "Staff:", 3);
        cmbStaff = new JComboBox<>();
        cmbStaff.setFont(UIConstants.TABLE_CONTENT_FONT);
        gbc.gridx = 1;
        gbc.gridy = 3;
        fieldsPanel.add(cmbStaff, gbc);
        
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
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(imagePanel, BorderLayout.EAST);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnSave = new JButton(isEditMode ? "Update Expense" : "Save Expense");
        btnSave.setFont(UIConstants.BUTTON_FONT);
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(UIConstants.PRIMARY_COLOR);
        btnSave.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V, 
            UIConstants.BUTTON_PADDING_H, 
            UIConstants.BUTTON_PADDING_V, 
            UIConstants.BUTTON_PADDING_H
        ));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSave.setBackground(UIConstants.ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnSave.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
        
        btnSave.addActionListener(e -> saveExpense());
        
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(UIConstants.BUTTON_FONT);
        btnCancel.setForeground(UIConstants.TEXT_COLOR);
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(
                UIConstants.BUTTON_PADDING_V - 1, 
                UIConstants.BUTTON_PADDING_H - 1, 
                UIConstants.BUTTON_PADDING_V - 1, 
                UIConstants.BUTTON_PADDING_H - 1
            )
        ));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancel.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnCancel.setBackground(Color.WHITE);
            }
        });
        
        btnCancel.addActionListener(e -> router.navigate("/expenses"));
        
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(Box.createHorizontalStrut(10));
        buttonsPanel.add(btnSave);
        
        formPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }
    
    /**
     * Add a label to the form.
     * 
     * @param panel The panel to add the label to
     * @param text The label text
     * @param row The row to add the label to
     */
    private void addFormLabel(JPanel panel, String text, int row) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.BUTTON_FONT);
        label.setForeground(UIConstants.TEXT_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
    }
    
    /**
     * Load staff members into the staff combo box.
     */
    private void loadStaffComboBox() {
        try {
            List<Staff> staffList = staffController.getAllStaff();
            cmbStaff.removeAllItems();
            
            for (Staff staff : staffList) {
                // TODO: Implement Permission

                Staff user = SessionManager.getCurrentUser();

                if (user.getId() == staff.getId() || user.getRole().equalsIgnoreCase("admin")) {
                    cmbStaff.addItem(new StaffComboItem(staff.getId(), staff.getName()));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExpenseFormPanel.class.getName()).log(Level.SEVERE, "Error loading staff list", ex);
            JOptionPane.showMessageDialog(this, "Error loading staff list: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Save the expense data.
     */
    private void saveExpense() {
        try {
            // Validate input
            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();
            String amountText = txtAmount.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                txtName.requestFocus();
                return;
            }
            
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Description is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                txtDescription.requestFocus();
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    throw new NumberFormatException("Amount must be greater than zero.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid positive number.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                txtAmount.requestFocus();
                return;
            }
            
            if (cmbStaff.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select a staff member.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                cmbStaff.requestFocus();
                return;
            }
            
            StaffComboItem selectedStaff = (StaffComboItem) cmbStaff.getSelectedItem();
            int staffId = selectedStaff.getId();
            
            if (isEditMode) {
                // Update existing expense
                boolean updated = expenseController.updateExpense(expenseId, name, description, amount, staffId);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Expense updated successfully.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    router.navigate("/expenses");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update expense.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Add new expense
                boolean added = expenseController.addExpense(name, description, amount, staffId);
                if (added) {
                    JOptionPane.showMessageDialog(this, "Expense added successfully.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    router.navigate("/expenses");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add expense.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ExpenseFormPanel.class.getName()).log(Level.SEVERE, "Error saving expense", ex);
            JOptionPane.showMessageDialog(this, "Error saving expense: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
     * Inner class for staff combo box items.
     */
    private static class StaffComboItem {
        private final int id;
        private final String name;
        
        public StaffComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
} 