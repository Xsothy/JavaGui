package View.Dashboard;

import Controller.SaleController;
import Model.SaleDetailWithProduct;
import Model.SaleWithDetails;
import Support.Router;
import Support.UIConstants;
import View.Layout.DashboardLayout;
import View.NavigatePanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for displaying sale details.
 */
public class SaleDetailsPanel extends DashboardLayout {
    private final SaleController saleController;
    private final SaleWithDetails sale;
    private final SimpleDateFormat dateFormat;
    
    /**
     * Constructor for SaleDetailsPanel.
     * 
     * @param sale The sale to display
     */
    public SaleDetailsPanel(SaleWithDetails sale) {
        super();
        this.saleController = new SaleController();
        this.sale = sale;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        
        // Create main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, UIConstants.SECTION_SPACING));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.SECTION_SPACING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING,
                UIConstants.CONTENT_PADDING
        ));
        
        // Add sale info panel
        JPanel infoPanel = createSaleInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Add sale items table
        JPanel itemsPanel = createSaleItemsPanel();
        mainPanel.add(itemsPanel, BorderLayout.CENTER);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        
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
        
        // Left side: Back button
        JButton backButton = new JButton("Back to Sales");
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
        
        backButton.addActionListener(e -> Router.navigate("dashboard/sales"));
        
        // Right side: Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Sale Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Sale #" + sale.getSale().getId());
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
     * Create the sale information panel.
     * 
     * @return The sale info panel
     */
    private JPanel createSaleInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Sale info title
        JLabel infoTitleLabel = new JLabel("Sale Information");
        infoTitleLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        infoTitleLabel.setForeground(UIConstants.TEXT_COLOR);
        infoTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Info grid
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 10));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Sale ID and Date
        JPanel idPanel = createInfoFieldPanel("Sale ID:", String.valueOf(sale.getSale().getId()));
        JPanel datePanel = createInfoFieldPanel("Date:", dateFormat.format(sale.getSale().getDate()));
        
        // Staff info
        JPanel staffPanel = createInfoFieldPanel("Staff:", sale.getStaffName());
        JPanel totalItemsPanel = createInfoFieldPanel("Total Items:", String.valueOf(sale.getTotalItems()));
        
        // Totals
        JPanel totalPanel = createInfoFieldPanel("Total Amount:", "$" + sale.getSale().getTotal().toString());
        JPanel uniqueItemsPanel = createInfoFieldPanel("Unique Products:", String.valueOf(sale.getUniqueProducts()));
        
        gridPanel.add(idPanel);
        gridPanel.add(datePanel);
        gridPanel.add(staffPanel);
        gridPanel.add(totalItemsPanel);
        gridPanel.add(totalPanel);
        gridPanel.add(uniqueItemsPanel);
        
        infoPanel.add(infoTitleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(gridPanel);
        
        return infoPanel;
    }
    
    /**
     * Create an info field panel with a label and value.
     * 
     * @param labelText The label text
     * @param valueText The value text
     * @return The info field panel
     */
    private JPanel createInfoFieldPanel(String labelText, String valueText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.TABLE_CONTENT_FONT.deriveFont(Font.BOLD));
        label.setForeground(UIConstants.TEXT_COLOR);
        
        JLabel value = new JLabel(valueText);
        value.setFont(UIConstants.TABLE_CONTENT_FONT);
        value.setForeground(UIConstants.TEXT_COLOR);
        
        panel.add(label, BorderLayout.WEST);
        panel.add(value, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the sale items panel with a table.
     * 
     * @return The sale items panel
     */
    private JPanel createSaleItemsPanel() {
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Items title
        JLabel itemsTitleLabel = new JLabel("Sale Items");
        itemsTitleLabel.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD));
        itemsTitleLabel.setForeground(UIConstants.TEXT_COLOR);
        
        // Create table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        // Add columns
        model.addColumn("Product ID");
        model.addColumn("Product Name");
        model.addColumn("Unit Price");
        model.addColumn("Quantity");
        model.addColumn("Total");
        
        // Add rows
        for (SaleDetailWithProduct detail : sale.getSaleDetails()) {
            model.addRow(new Object[]{
                detail.getProduct().getId(),
                detail.getProduct().getName(),
                "$" + detail.getProduct().getPrice(),
                detail.getSaleDetail().getQuantity(),
                "$" + detail.getLineTotal()
            });
        }
        
        // Create and configure table
        JTable itemsTable = new JTable(model);
        itemsTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        itemsTable.setShowGrid(true);
        itemsTable.setGridColor(UIConstants.BORDER_COLOR);
        itemsTable.setFont(UIConstants.TABLE_CONTENT_FONT);
        itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        itemsTable.setFillsViewportHeight(true);
        
        // Style the header
        itemsTable.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
        itemsTable.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        itemsTable.getTableHeader().setForeground(Color.WHITE);
        itemsTable.getTableHeader().setPreferredSize(
            new Dimension(itemsTable.getTableHeader().getPreferredSize().width, UIConstants.TABLE_HEADER_HEIGHT)
        );
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < itemsTable.getColumnCount(); i++) {
            itemsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set column widths
        itemsTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Product ID
        itemsTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Product Name
        itemsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Unit Price
        itemsTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Quantity
        itemsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Total
        
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        itemsPanel.add(itemsTitleLabel, BorderLayout.NORTH);
        itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        itemsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return itemsPanel;
    }
} 