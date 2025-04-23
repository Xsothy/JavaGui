package View.Dashboard;

import Controller.ExpenseController;
import Model.Expense;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

/**
 * Panel for displaying expense details.
 */
public class ExpenseDetailPanel extends DashboardLayout {
    private final ExpenseController expenseController;
    private final Expense expense;
    
    /**
     * Constructor for ExpenseDetailPanel.
     * 
     * @param expense The expense to display
     */
    public ExpenseDetailPanel(Expense expense) {
        super();
        this.expenseController = new ExpenseController();
        this.expense = expense;
    }
    
    @Override
    public void render() {
        super.render();
    }
    
    @Override
    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Create header panel with title and back button
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create details panel
        JPanel detailsPanel = createDetailsPanel();
        contentPanel.add(detailsPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Create the header panel with title and back button.
     * 
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
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
        
        // Left side of header: Back button
        JButton backButton = new JButton("Back to Expenses");
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
        
        backButton.addActionListener(e -> {
            Router.navigate("dashboard/expenses");
        });
        
        // Right side of header: Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Expense Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("View expense information");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create the details panel with expense information.
     * 
     * @return The details panel
     */
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SECTION_SPACING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING,
            UIConstants.CONTENT_PADDING
        ));
        
        // Details container
        JPanel detailsContainer = new JPanel(new GridBagLayout());
        detailsContainer.setBackground(Color.WHITE);
        detailsContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("Expense ID:");
        idLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel idValue = new JLabel(String.valueOf(expense.getId()));
        idValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(idValue, gbc);
        
        // Title field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(titleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel titleValue = new JLabel(expense.getName());
        titleValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(titleValue, gbc);
        
        // Amount field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(amountLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel amountValue = new JLabel(expense.getAmount().toString());
        amountValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(amountValue, gbc);
        
        // Date field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JLabel dateValue = new JLabel(dateFormat.format(expense.getDate()));
        dateValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        detailsContainer.add(dateValue, gbc);
        
        // Description field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        detailsContainer.add(descriptionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JTextArea descriptionValue = new JTextArea(expense.getDescription());
        descriptionValue.setFont(UIConstants.TABLE_CONTENT_FONT);
        descriptionValue.setEditable(false);
        descriptionValue.setLineWrap(true);
        descriptionValue.setWrapStyleWord(true);
        descriptionValue.setBackground(Color.WHITE);
        descriptionValue.setBorder(null);
        descriptionValue.setRows(3);
        detailsContainer.add(descriptionValue, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Edit button
        JButton editButton = new JButton("Edit Expense");
        editButton.setFont(UIConstants.BUTTON_FONT);
        editButton.setForeground(Color.WHITE);
        editButton.setBackground(UIConstants.PRIMARY_COLOR);
        editButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                editButton.setBackground(UIConstants.ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                editButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
        
        editButton.addActionListener(e -> {
            Router.navigate("dashboard/expenses/edit/" + expense.getId());
        });
        
        // Delete button
        JButton deleteButton = new JButton("Delete Expense");
        deleteButton.setFont(UIConstants.BUTTON_FONT);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(UIConstants.DANGER_COLOR);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H,
            UIConstants.BUTTON_PADDING_V,
            UIConstants.BUTTON_PADDING_H
        ));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this expense?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    expenseController.deleteExpense(expense.getId());
                        JOptionPane.showMessageDialog(
                            this,
                            "Expense deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        Router.navigate("dashboard/expenses");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        buttonsPanel.add(editButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(deleteButton);
        
        // Add details container and buttons panel to the details panel
        detailsPanel.add(detailsContainer);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(buttonsPanel);
        
        return detailsPanel;
    }
} 