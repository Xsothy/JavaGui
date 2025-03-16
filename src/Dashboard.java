import Components.ExpenseFormPanel;
import Components.ExpensePanel;
import Components.StaffFormPanel;
import Components.StaffPanel;
import Controller.ExpenseController;
import Controller.StaffController;
import Model.Staff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main dashboard of the application.
 */
public class Dashboard extends javax.swing.JFrame {
    private JPanel contentPanel;
    private Router router;
    private JButton btnStaff;
    private JButton btnExpense;
    private JButton btnEditProfile;
    private JButton btnLogout;
    private JButton currentSelectedButton;
    
    /**
     * Creates a new Dashboard.
     */
    public Dashboard() {
        initComponents();
        setupRouter();
        
        // Show staff panel by default
        btnStaff.doClick();
        
        // Center the frame on the screen
        setLocationRelativeTo(null);
    }
     
    /**
     * Initialize the components.
     */
    private void initComponents() {
        setTitle("Dashboard - Staff and Expense Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create the sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIConstants.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.BORDER_COLOR));
        
        // Add sidebar heading
        JLabel sidebarTitle = new JLabel("Management System");
        sidebarTitle.setFont(UIConstants.TITLE_FONT);
        sidebarTitle.setForeground(Color.WHITE);
        sidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        
        // Create sidebar buttons
        btnStaff = new JButton("Staff Management");
        btnExpense = new JButton("Expense Management");
        btnEditProfile = new JButton("Edit Profile");
        btnLogout = new JButton("Logout");
        
        // Style the buttons
        JButton[] buttons = {btnStaff, btnExpense, btnLogout, btnEditProfile};
        for (JButton button : buttons) {
            applyButtonStyles(button);
        }
        
        // Add special styling to logout
        btnLogout.setBackground(UIConstants.DANGER_COLOR);
        btnEditProfile.setBackground(UIConstants.INFO_COLOR);
        
        // Add action listeners
        btnStaff.addActionListener(e -> {
            updateButtonSelectionState(btnStaff);
            router.navigate("/staffs");
        });
        
        btnExpense.addActionListener(e -> {
            updateButtonSelectionState(btnExpense);
            router.navigate("/expenses");
        });

        btnEditProfile.addActionListener(e -> {
            updateButtonSelectionState(btnEditProfile);
            Staff staff = SessionManager.getCurrentUser();
            router.navigate("/staffs/edit/" + staff.getId());
        });


        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Clear the session
                SessionManager.logout();
                
                // Close dashboard and show login
                dispose();
                new Login().setVisible(true);
            }
        });
        
        // Add to sidebar with proper spacing
        sidebar.add(sidebarTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnStaff);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnExpense);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnEditProfile);
        sidebar.add(btnLogout);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Add the components to the frame
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Apply consistent styling to a button.
     * 
     * @param button The button to style
     */
    private void applyButtonStyles(JButton button) {
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(UIConstants.SIDEBAR_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != currentSelectedButton) {
                    button.setBackground(UIConstants.ACCENT_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != currentSelectedButton) {
                    if (button == btnLogout) {
                        button.setBackground(UIConstants.DANGER_COLOR);
                    }
                    else if(button == btnEditProfile) {
                        button.setBackground(UIConstants.INFO_COLOR);
                    }
                    else {
                        button.setBackground(UIConstants.SIDEBAR_COLOR);
                    }
                }
            }
        });
    }

    /**
     * Update the visual state of buttons when selected.
     * 
     * @param selectedButton The currently selected button
     */
    private void updateButtonSelectionState(JButton selectedButton) {
        // Reset previous selected button
        if (currentSelectedButton != null) {
            if (currentSelectedButton == btnLogout) {
                currentSelectedButton.setBackground(UIConstants.DANGER_COLOR);
            }
            else if(currentSelectedButton == btnEditProfile) {
                btnStaff.setBackground(UIConstants.INFO_COLOR);
            }
            else {
                currentSelectedButton.setBackground(UIConstants.SIDEBAR_COLOR);
            }
        }
        
        // Set current selected button
        currentSelectedButton = selectedButton;
        currentSelectedButton.setBackground(UIConstants.PRIMARY_COLOR);
    }

    /**
     * Set up the router for navigation.
     */
    private void setupRouter() {
        router = new Router(contentPanel);
        
        // Register routes
        router.register("/staffs", new StaffPanel(router));
        router.register("/staffs/{id}", parameters -> {
            return new StaffController().show(parameters, router);
        });
        router.register("/staffs/add", new StaffFormPanel(router));
        router.register("/staffs/edit/{id}", parameters -> {
            return new StaffController().edit(parameters, router);
        });
        router.register("/expenses", new ExpensePanel(router));
        router.register("/expenses/add", new ExpenseFormPanel(router));
        router.register("/expenses/edit/{id}", parameters -> {
            return new ExpenseController().edit(parameters, router);
        });
        
        // Default route
        router.navigate("/staffs");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Set some global UI defaults
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
