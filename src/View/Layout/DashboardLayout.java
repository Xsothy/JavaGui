package View.Layout;

import Model.Staff;
import Support.Router;
import Support.SessionManager;
import Support.UIConstants;
import View.NavigatePanel;
import java.awt.*;
import javax.swing.*;

public class DashboardLayout extends NavigatePanel {
    private final JButton btnStaff;
    private final JButton btnExpense;
    private final JButton btnProduct;
    private final JButton btnCategory;
    private final JButton btnSale;
    private final JButton btnSaleDashboard;
    private final JButton btnEditProfile;
    private JButton currentSelectedButton;

    public DashboardLayout() {
        super();
        setLayout(new BorderLayout());
        setSize(1200, 700);

        // Create the sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIConstants.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension((int) (getWidth() * 0.2), getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.BORDER_COLOR));

        // Sidebar title
        JLabel sidebarTitle = new JLabel("Management System");
        sidebarTitle.setFont(UIConstants.TITLE_FONT);
        sidebarTitle.setForeground(Color.WHITE);
        sidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        // Sidebar buttons
        btnStaff = new JButton("Staff Management");
        btnExpense = new JButton("Expense Management");
        btnProduct = new JButton("Product Management");
        btnCategory = new JButton("Category Management");
        btnSale = new JButton("Sales Management");
        btnSaleDashboard = new JButton("Sales Dashboard");
        btnEditProfile = new JButton("Edit Profile");
        JButton btnBack = new JButton("Back");
        applyButtonStyles(btnStaff);
        applyButtonStyles(btnExpense);
        applyButtonStyles(btnProduct);
        applyButtonStyles(btnCategory);
        applyButtonStyles(btnSale);
        applyButtonStyles(btnSaleDashboard);
        applyButtonStyles(btnEditProfile);
        applyButtonStyles(btnBack);

        btnBack.setBackground(UIConstants.DANGER_COLOR);
        btnEditProfile.setBackground(UIConstants.INFO_COLOR);

        btnStaff.addActionListener(e -> {
            Router.navigate("dashboard/staffs");
        });

        btnExpense.addActionListener(e -> {
            Router.navigate("dashboard/expenses");
        });

        btnProduct.addActionListener(e -> {
            Router.navigate("dashboard/products");
        });

        btnCategory.addActionListener(e -> {
            Router.navigate("dashboard/categories");
        });

        btnSale.addActionListener(e -> {
            Router.navigate("dashboard/sales");
        });

        btnSaleDashboard.addActionListener(e -> {
            Router.navigate("dashboard/sales/dashboard");
        });

        btnEditProfile.addActionListener(e -> {
            Staff staff = SessionManager.getCurrentUser();
            Router.navigate("dashboard/staffs/edit/" + staff.getId());
        });

        btnBack.addActionListener(e -> {
            Router.navigate("/");
        });

        // Add to sidebar
        sidebar.add(sidebarTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnStaff);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnExpense);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnProduct);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnCategory);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSale);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSaleDashboard);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnEditProfile);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnBack);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add to main layout
        add(sidebar, BorderLayout.WEST);
    }

    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        return contentPanel;
    }
    /**
     * Apply consistent styling to a button.
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
    }

    @Override
    public void render() {
        // Content panel
        NavigatePanel contentPanel = getContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        if (Router.getCurrentRoute().startsWith("dashboard/staffs")) {
            currentSelectedButton = btnStaff;
        } else if (Router.getCurrentRoute().startsWith("dashboard/expenses")) {
            currentSelectedButton = btnExpense;
        } else if (Router.getCurrentRoute().startsWith("dashboard/categories")) {
            currentSelectedButton = btnCategory;
        } else if (Router.getCurrentRoute().startsWith("dashboard/products")) {
            currentSelectedButton = btnProduct;
        } else if (Router.getCurrentRoute().equals("dashboard/sales/dashboard")) {
            currentSelectedButton = btnSaleDashboard;
        } else if (Router.getCurrentRoute().startsWith("dashboard/sales")) {
            currentSelectedButton = btnSale;
        }
        updateButtonSelectionState(currentSelectedButton);
        revalidate();
        repaint();
    }

    /**
     * Update the visual state of buttons when selected.
     */
    private void updateButtonSelectionState(JButton selectedButton) {
        btnStaff.setBackground(UIConstants.SIDEBAR_COLOR);
        btnExpense.setBackground(UIConstants.SIDEBAR_COLOR);
        btnProduct.setBackground(UIConstants.SIDEBAR_COLOR);
        btnCategory.setBackground(UIConstants.SIDEBAR_COLOR);
        btnSale.setBackground(UIConstants.SIDEBAR_COLOR);
        btnSaleDashboard.setBackground(UIConstants.SIDEBAR_COLOR);

        if (currentSelectedButton == null) return;
        currentSelectedButton = selectedButton;
        currentSelectedButton.setBackground(UIConstants.PRIMARY_COLOR);
    }
}
