package Controller;

import Model.*;
import Repository.SaleRepository;
import Repository.ProductRepository;
import Support.SessionManager;
import View.Dashboard.SaleDetailsPanel;
import View.Dashboard.SaleFormPanel;
import View.NavigatePanel;
import java.math.BigDecimal;
import java.util.*;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.RoundingMode;
import Support.DB;

/**
 * Controller class for managing sales.
 */
public class SaleController {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    
    /**
     * Constructor for SaleController.
     */
    public SaleController() {
        this.saleRepository = new SaleRepository();
        this.productRepository = new ProductRepository();
    }
    
    /**
     * Get all sales with staff information.
     * 
     * @return List of sales with staff information
     */
    public List<SaleWithStaff> getAllSalesWithStaff() {
        return saleRepository.getAllSalesWithStaff();
    }
    
    /**
     * Get all sales.
     * 
     * @return List of all sales
     */
    public List<Sale> getAllSales() {
        return saleRepository.getAllSales();
    }
    
    /**
     * Get a sale by ID.
     * 
     * @param saleId The sale ID
     * @return Optional containing the sale if found, empty otherwise
     */
    public Optional<Sale> getSaleById(int saleId) {
        if (saleId <= 0) {
            return Optional.empty();
        }
        
        return saleRepository.getSaleById(saleId);
    }
    
    /**
     * Get a sale with staff information by ID.
     * 
     * @param saleId The sale ID
     * @return Optional containing the sale with staff information if found, empty otherwise
     */
    public Optional<SaleWithStaff> getSaleWithStaffById(int saleId) {
        if (saleId <= 0) {
            return Optional.empty();
        }
        
        return saleRepository.getSaleWithStaffById(saleId);
    }
    
    /**
     * Get a complete sale with all details by ID.
     * 
     * @param saleId The sale ID
     * @return Optional containing the complete sale if found, empty otherwise
     */
    public Optional<SaleWithDetails> getSaleWithDetails(int saleId) {
        if (saleId <= 0) {
            return Optional.empty();
        }
        
        return saleRepository.getSaleWithDetails(saleId);
    }
    
    /**
     * Get all details for a sale.
     * 
     * @param saleId The sale ID
     * @return List of sale details
     */
    public List<SaleDetail> getSaleDetails(int saleId) {
        if (saleId <= 0) {
            throw new IllegalArgumentException("Invalid sale ID");
        }
        
        return saleRepository.getSaleDetails(saleId);
    }
    
    /**
     * Get all details for a sale with product information.
     * 
     * @param saleId The sale ID
     * @return List of sale details with product information
     */
    public List<SaleDetailWithProduct> getSaleDetailsWithProducts(int saleId) {
        if (saleId <= 0) {
            throw new IllegalArgumentException("Invalid sale ID");
        }
        
        return saleRepository.getSaleDetailsWithProducts(saleId);
    }
    
    /**
     * Create a new sale.
     * 
     * @param saleItems List of maps containing product IDs and quantities
     * @return The ID of the created sale, or -1 if creation failed
     */
    public int createSale(List<Map<String, Object>> saleItems) {
        if (saleItems == null || saleItems.isEmpty()) {
            throw new IllegalArgumentException("Sale must have at least one item");
        }
        
        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> item : saleItems) {
            int productId = (int) item.get("productId");
            int quantity = (int) item.get("quantity");
            
            if (productId <= 0) {
                throw new IllegalArgumentException("Invalid product ID");
            }
            
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            
            // Get product to check if it exists and has enough stock
            Optional<Product> productOpt = productRepository.getProductById(productId);
            if (productOpt.isEmpty()) {
                throw new IllegalArgumentException("Product not found: " + productId);
            }
            
            Product product = productOpt.get();
            if (product.getStockQuantity() < quantity) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            
            // Add to total
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(quantity));
            total = total.add(itemTotal);
        }
        
        // Get current staff ID from session
        int staffId = SessionManager.getCurrentUser().getId();
        
        // Create the sale
        return saleRepository.createSale(new Date(), total, staffId, saleItems);
    }
    
    /**
     * Delete a sale.
     * 
     * @param saleId The ID of the sale to delete
     * @return True if the deletion was successful, false otherwise
     */
    public boolean deleteSale(int saleId) {
        if (saleId <= 0) {
            throw new IllegalArgumentException("Invalid sale ID");
        }
        
        Optional<Sale> sale = getSaleById(saleId);
        if (sale.isEmpty()) {
            throw new IllegalArgumentException("Sale not found");
        }
        
        return saleRepository.deleteSale(saleId);
    }
    
    /**
     * Search for sales by staff name or username.
     * 
     * @param searchTerm The search term
     * @return List of sales matching the search term
     */
    public List<SaleWithStaff> searchSales(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllSalesWithStaff();
        }
        
        return saleRepository.searchSales(searchTerm.trim());
    }
    
    /**
     * Filter sales by date range.
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of sales in the date range
     */
    public List<SaleWithStaff> getSalesByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return getAllSalesWithStaff();
        }
        
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        return saleRepository.getSalesByDateRange(startDate, endDate);
    }
    
    /**
     * Show sale details panel.
     * 
     * @param params Route parameters
     * @return The sale details panel
     */
    public NavigatePanel show(Map<String, String> params) {
        int saleId = Integer.parseInt(params.get("id"));
        
        Optional<SaleWithDetails> saleOpt = getSaleWithDetails(saleId);
        if (saleOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sale not found", "Error", JOptionPane.ERROR_MESSAGE);
            return new NavigatePanel();
        }
        
        return new SaleDetailsPanel(saleOpt.get());
    }
    
    /**
     * Show sale creation form.
     * 
     * @return The sale form panel
     */
    public NavigatePanel create() {
        return new SaleFormPanel();
    }
    
    /**
     * Get the total amount of all sales within a date range.
     *
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return The total sales amount
     */
    public BigDecimal getTotalSalesAmount(Date startDate, Date endDate) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            Connection connection = DB.getInstance().getConnection();
            String query = "SELECT SUM(total) AS total_amount FROM sales WHERE date BETWEEN ? AND ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, new Timestamp(startDate.getTime()));
            statement.setTimestamp(2, new Timestamp(endDate.getTime()));
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double amount = resultSet.getDouble("total_amount");
                total = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching total sales amount: " + e.getMessage());
        }
        return total;
    }
    
    /**
     * Get the total count of sales within a date range.
     *
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return The total number of sales
     */
    public int getTotalSalesCount(Date startDate, Date endDate) {
        int count = 0;
        try {
            Connection connection = DB.getInstance().getConnection();
            String query = "SELECT COUNT(*) AS sale_count FROM sales WHERE date BETWEEN ? AND ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, new Timestamp(startDate.getTime()));
            statement.setTimestamp(2, new Timestamp(endDate.getTime()));
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("sale_count");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching sales count: " + e.getMessage());
        }
        return count;
    }
} 