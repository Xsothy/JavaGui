package View.Dashboard;

import Controller.SaleController;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

/**
 * Dashboard panel to display sales statistics with filters.
 */
public class SaleDashboardPanel extends DashboardLayout {
    private final SaleController saleController;
    private JRadioButton dailyRadioButton;
    private JRadioButton monthlyRadioButton;
    private JRadioButton customRadioButton;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JPanel customDatePanel;
    private JLabel totalAmountValueLabel;
    private JLabel totalSalesValueLabel;

    /**
     * Constructor for SaleDashboardPanel.
     */
    public SaleDashboardPanel() {
        super();
        this.saleController = new SaleController();
    }

    @Override
    public void render() {
        super.render();
        updateStatistics();
    }

    @Override
    public NavigatePanel getContentPanel() {
        NavigatePanel contentPanel = new NavigatePanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBackground(Color.WHITE);

        // Create header panel with title and Go to Sales button
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));

        // Add filter panel
        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, UIConstants.SECTION_SPACING)));

        // Add statistics cards
        JPanel cardsPanel = createStatisticsCardsPanel();
        mainPanel.add(cardsPanel);

        contentPanel.add(mainPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    /**
     * Create the header panel with title and navigation button.
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

        // Left side: Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Sales Dashboard");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("View sales statistics and metrics");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.LIGHT_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        // Right side: Go to Sales button
        JButton goToSalesButton = new JButton("Go to Sales");
        goToSalesButton.setFont(UIConstants.BUTTON_FONT);
        goToSalesButton.setForeground(Color.WHITE);
        goToSalesButton.setBackground(UIConstants.PRIMARY_COLOR);
        goToSalesButton.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H,
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H
        ));
        goToSalesButton.setFocusPainted(false);
        goToSalesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToSalesButton.addActionListener(e -> Router.navigate("dashboard/sales"));

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(goToSalesButton, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Create the filter panel with date filter options.
     *
     * @return The filter panel
     */
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Filter title
        JLabel filterTitleLabel = new JLabel("Filter Sales Data");
        filterTitleLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        filterTitleLabel.setForeground(UIConstants.TEXT_COLOR);
        filterTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Radio buttons for filter options
        JPanel radioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        radioButtonsPanel.setBackground(Color.WHITE);
        radioButtonsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        radioButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dailyRadioButton = new JRadioButton("Daily");
        monthlyRadioButton = new JRadioButton("Monthly");
        customRadioButton = new JRadioButton("Custom Date Range");

        dailyRadioButton.setFont(UIConstants.TABLE_CONTENT_FONT);
        monthlyRadioButton.setFont(UIConstants.TABLE_CONTENT_FONT);
        customRadioButton.setFont(UIConstants.TABLE_CONTENT_FONT);

        dailyRadioButton.setBackground(Color.WHITE);
        monthlyRadioButton.setBackground(Color.WHITE);
        customRadioButton.setBackground(Color.WHITE);

        // Group the radio buttons
        ButtonGroup filterGroup = new ButtonGroup();
        filterGroup.add(dailyRadioButton);
        filterGroup.add(monthlyRadioButton);
        filterGroup.add(customRadioButton);

        // Default selection
        dailyRadioButton.setSelected(true);

        radioButtonsPanel.add(dailyRadioButton);
        radioButtonsPanel.add(monthlyRadioButton);
        radioButtonsPanel.add(customRadioButton);

        // Custom date range panel
        customDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        customDatePanel.setBackground(Color.WHITE);
        customDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        customDatePanel.setVisible(false);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        fromDateChooser = new JDateChooser();
        fromDateChooser.setPreferredSize(new Dimension(150, 30));
        fromDateChooser.setDate(new Date());

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(UIConstants.TABLE_CONTENT_FONT);
        toDateChooser = new JDateChooser();
        toDateChooser.setPreferredSize(new Dimension(150, 30));
        toDateChooser.setDate(new Date());

        customDatePanel.add(fromLabel);
        customDatePanel.add(fromDateChooser);
        customDatePanel.add(toLabel);
        customDatePanel.add(toDateChooser);

        // Apply button
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setFont(UIConstants.BUTTON_FONT);
        applyButton.setForeground(Color.WHITE);
        applyButton.setBackground(UIConstants.PRIMARY_COLOR);
        applyButton.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H,
                UIConstants.BUTTON_PADDING_V,
                UIConstants.BUTTON_PADDING_H
        ));
        applyButton.setFocusPainted(false);
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(applyButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add action listeners
        customRadioButton.addActionListener(e -> customDatePanel.setVisible(customRadioButton.isSelected()));
        
        dailyRadioButton.addActionListener(e -> customDatePanel.setVisible(false));
        
        monthlyRadioButton.addActionListener(e -> customDatePanel.setVisible(false));
        
        applyButton.addActionListener(e -> updateStatistics());

        // Add components to filter panel
        filterPanel.add(filterTitleLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(radioButtonsPanel);
        filterPanel.add(customDatePanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(buttonPanel);

        return filterPanel;
    }

    /**
     * Create the statistics cards panel.
     *
     * @return The statistics cards panel
     */
    private JPanel createStatisticsCardsPanel() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Total Amount Card
        JPanel totalAmountCard = createCard("Total Amount", "$0.00", UIConstants.PRIMARY_COLOR);
        totalAmountValueLabel = (JLabel) ((JPanel) totalAmountCard.getComponent(1)).getComponent(0);

        // Total Sales Card
        JPanel totalSalesCard = createCard("Total Sales", "0", UIConstants.SUCCESS_COLOR);
        totalSalesValueLabel = (JLabel) ((JPanel) totalSalesCard.getComponent(1)).getComponent(0);

        cardsPanel.add(totalAmountCard);
        cardsPanel.add(totalSalesCard);

        return cardsPanel;
    }

    /**
     * Create a statistics card.
     *
     * @param title The card title
     * @param value The initial value
     * @param color The card color
     * @return The card panel
     */
    private JPanel createCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Card title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Card value
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setBackground(Color.WHITE);
        valuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIConstants.TITLE_FONT.deriveFont(Font.BOLD, 32f));
        valueLabel.setForeground(color);

        valuePanel.add(valueLabel);

        card.add(titleLabel);
        card.add(valuePanel);

        return card;
    }

    /**
     * Update the statistics based on the selected filter.
     */
    private void updateStatistics() {
        Date startDate = null;
        Date endDate = null;
        Calendar calendar = Calendar.getInstance();

        if (dailyRadioButton.isSelected()) {
            // Set start date to beginning of today
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            startDate = calendar.getTime();

            // Set end date to end of today
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            endDate = calendar.getTime();
        } else if (monthlyRadioButton.isSelected()) {
            // Set start date to beginning of current month
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            startDate = calendar.getTime();

            // Set end date to end of current month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            endDate = calendar.getTime();
        } else {
            // Use custom date range
            startDate = fromDateChooser.getDate();
            
            endDate = toDateChooser.getDate();
            
            // Set start date to beginning of day
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            startDate = calendar.getTime();
            
            // Set end date to end of day
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            endDate = calendar.getTime();
        }

        // Fetch sales data from the controller
        BigDecimal totalAmount = saleController.getTotalSalesAmount(startDate, endDate);
        int totalSales = saleController.getTotalSalesCount(startDate, endDate);

        // Update the UI
        totalAmountValueLabel.setText("$" + totalAmount.toString());
        totalSalesValueLabel.setText(String.valueOf(totalSales));
    }
} 