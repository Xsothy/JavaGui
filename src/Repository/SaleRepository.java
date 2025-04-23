package Repository;

import Model.*;
import Support.DB;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Repository class for handling database operations related to sales.
 */
public class SaleRepository {
    private final StaffRepository staffRepository;
    private final ProductRepository productRepository;
    private final SimpleDateFormat dateFormat;
    
    /**
     * Constructor for SaleRepository.
     */
    public SaleRepository() {
        this.staffRepository = new StaffRepository();
        this.productRepository = new ProductRepository();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * Get all sales from the database.
     * 
     * @return List of sales
     */
    public List<Sale> getAllSales() {
        return DB.unsafeExecute(
            connection -> {
                List<Sale> sales = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT ID, Date, Total, SID FROM sales ORDER BY Date DESC"
                );
                
                while (rs.next()) {
                    Date date;
                    try {
                        date = dateFormat.parse(rs.getString("Date"));
                    } catch (ParseException e) {
                        date = new Date(); // Fallback to current date if parse fails
                    }
                    
                    sales.add(
                        new Sale(
                            rs.getInt("ID"),
                            date,
                            rs.getBigDecimal("Total"),
                            rs.getInt("SID")
                        )
                    );
                }
                
                return sales;
            }
        );
    }
    
    /**
     * Get all sales with staff information.
     * 
     * @return List of sales with staff information
     */
    public List<SaleWithStaff> getAllSalesWithStaff() {
        return DB.unsafeExecute(
            connection -> {
                List<SaleWithStaff> sales = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT s.ID, s.Date, s.Total, s.SID, st.Name, st.Position, st.UserName, st.Password, st.Role " +
                    "FROM sales s " +
                    "LEFT JOIN staff st ON s.SID = st.ID " +
                    "ORDER BY s.Date DESC"
                );
                
                while (rs.next()) {
                    Date date;
                    try {
                        date = dateFormat.parse(rs.getString("Date"));
                    } catch (ParseException e) {
                        date = new Date(); // Fallback to current date if parse fails
                    }
                    
                    Sale sale = new Sale(
                        rs.getInt("ID"),
                        date,
                        rs.getBigDecimal("Total"),
                        rs.getInt("SID")
                    );

                    Staff staff = null;
                    if (rs.getInt("SID") > 0) {
                        staff = new Staff(
                            rs.getString("Name"),
                            rs.getString("Position"),
                            rs.getString("UserName"),
                            rs.getString("Password"),
                            rs.getString("Role")
                        );
                    }
                    
                    sales.add(new SaleWithStaff(sale, staff));
                }
                
                return sales;
            }
        );
    }
    
    /**
     * Get a sale by ID.
     * 
     * @param saleId The sale ID
     * @return Optional containing the sale if found, empty otherwise
     */
    public Optional<Sale> getSaleById(int saleId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT ID, Date, Total, SID FROM sales WHERE ID = ?"
                );
                stmt.setInt(1, saleId);
                ResultSet rs = stmt.executeQuery();
                
                if (!rs.next()) {
                    return Optional.empty();
                }
                
                Date date;
                try {
                    date = dateFormat.parse(rs.getString("Date"));
                } catch (ParseException e) {
                    date = new Date(); // Fallback to current date if parse fails
                }
                
                return Optional.of(
                    new Sale(
                        rs.getInt("ID"),
                        date,
                        rs.getBigDecimal("Total"),
                        rs.getInt("SID")
                    )
                );
            }
        );
    }
    
    /**
     * Get a sale with staff information by ID.
     * 
     * @param saleId The sale ID
     * @return Optional containing the sale with staff information if found, empty otherwise
     */
    public Optional<SaleWithStaff> getSaleWithStaffById(int saleId) {
        return DB.unsafeExecute(
            connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.ID, s.Date, s.Total, s.SID, st.Name, st.Position, st.UserName, st.Password, st.Role " +
                    "FROM sales s " +
                    "LEFT JOIN staff st ON s.SID = st.ID " +
                    "WHERE s.ID = ?"
                );
                stmt.setInt(1, saleId);
                ResultSet rs = stmt.executeQuery();
                
                if (!rs.next()) {
                    return Optional.empty();
                }
                
                Date date;
                try {
                    date = dateFormat.parse(rs.getString("Date"));
                } catch (ParseException e) {
                    date = new Date(); // Fallback to current date if parse fails
                }
                
                Sale sale = new Sale(
                    rs.getInt("ID"),
                    date,
                    rs.getBigDecimal("Total"),
                    rs.getInt("SID")
                );
                
                Staff staff = null;
                if (rs.getInt("SID") > 0) {
                    staff = new Staff(
                        rs.getString("Name"),
                        rs.getString("Position"),
                        rs.getString("UserName"),
                        rs.getString("Password"),
                        rs.getString("Role")
                    );
                }
                
                return Optional.of(new SaleWithStaff(sale, staff));
            }
        );
    }
    
    /**
     * Get all details for a sale.
     * 
     * @param saleId The sale ID
     * @return List of sale details
     */
    public List<SaleDetail> getSaleDetails(int saleId) {
        return DB.unsafeExecute(
            connection -> {
                List<SaleDetail> details = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT ID, PID, Qty FROM sale_details WHERE ID = ?"
                );
                stmt.setInt(1, saleId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    details.add(
                        new SaleDetail(
                            rs.getInt("ID"),
                            rs.getInt("PID"),
                            rs.getInt("Qty")
                        )
                    );
                }
                
                return details;
            }
        );
    }
    
    /**
     * Get all details for a sale with product information.
     * 
     * @param saleId The sale ID
     * @return List of sale details with product information
     */
    public List<SaleDetailWithProduct> getSaleDetailsWithProducts(int saleId) {
        return DB.unsafeExecute(
            connection -> {
                List<SaleDetailWithProduct> details = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT sd.ID, sd.PID, sd.Qty, p.PName, p.Price, p.Image, p.CatID " +
                    "FROM sale_details sd " +
                    "JOIN product p ON sd.PID = p.PID " +
                    "WHERE sd.ID = ?"
                );
                stmt.setInt(1, saleId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    SaleDetail saleDetail = new SaleDetail(
                        rs.getInt("ID"),
                        rs.getInt("PID"),
                        rs.getInt("Qty")
                    );
                    
                    Product product = new Product(
                        rs.getInt("PID"),
                        rs.getString("PName"),
                        0, // Stock quantity not needed here
                        rs.getBigDecimal("Price"),
                        rs.getString("Image"),
                        rs.getInt("CatID")
                    );
                    
                    details.add(new SaleDetailWithProduct(saleDetail, product));
                }
                
                return details;
            }
        );
    }
    
    /**
     * Get a complete sale with all details and staff information.
     * 
     * @param saleId The sale ID
     * @return Optional containing the complete sale if found, empty otherwise
     */
    public Optional<SaleWithDetails> getSaleWithDetails(int saleId) {
        Optional<SaleWithStaff> saleWithStaffOpt = getSaleWithStaffById(saleId);
        
        if (saleWithStaffOpt.isEmpty()) {
            return Optional.empty();
        }
        
        SaleWithStaff saleWithStaff = saleWithStaffOpt.get();
        List<SaleDetailWithProduct> details = getSaleDetailsWithProducts(saleId);
        
        return Optional.of(new SaleWithDetails(
            saleWithStaff.getSale(),
            saleWithStaff.getStaff(),
            details
        ));
    }
    
    /**
     * Create a new sale with details.
     * 
     * @param date The sale date
     * @param total The total amount
     * @param staffId The staff ID
     * @param saleDetails List of sale details (product ID and quantity)
     * @return The ID of the created sale, or -1 if creation failed
     */
    public int createSale(Date date, BigDecimal total, int staffId, List<Map<String, Object>> saleDetails) {
        return DB.unsafeExecute(
            connection -> {
                // Insert the sale
                PreparedStatement saleStmt = connection.prepareStatement(
                    "INSERT INTO sales (Date, Total, SID) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                saleStmt.setString(1, dateFormat.format(date));
                saleStmt.setBigDecimal(2, total);
                saleStmt.setInt(3, staffId);
                
                int affectedRows = saleStmt.executeUpdate();
                if (affectedRows == 0) {
                    return -1;
                }
                
                // Get the generated sale ID
                ResultSet generatedKeys = saleStmt.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    return -1;
                }
                
                int saleId = generatedKeys.getInt(1);
                
                // Insert the sale details
                PreparedStatement detailStmt = connection.prepareStatement(
                    "INSERT INTO sale_details (ID, PID, Qty) VALUES (?, ?, ?)"
                );
                
                // Also update product stock quantities
                PreparedStatement updateStockStmt = connection.prepareStatement(
                    "UPDATE product SET Sqty = Sqty - ? WHERE PID = ?"
                );
                
                for (Map<String, Object> detail : saleDetails) {
                    int productId = (int) detail.get("productId");
                    int quantity = (int) detail.get("quantity");
                    
                    // Insert detail
                    detailStmt.setInt(1, saleId);
                    detailStmt.setInt(2, productId);
                    detailStmt.setInt(3, quantity);
                    detailStmt.executeUpdate();
                    
                    // Update stock
                    updateStockStmt.setInt(1, quantity);
                    updateStockStmt.setInt(2, productId);
                    updateStockStmt.executeUpdate();
                }
                
                return saleId;
            }
        );
    }
    
    /**
     * Delete a sale and its details.
     * 
     * @param saleId The sale ID
     * @return True if the sale was deleted, false otherwise
     */
    public boolean deleteSale(int saleId) {
        return DB.unsafeExecute(
            connection -> {
                // Get the sale details to restore product quantities
                PreparedStatement getDetailsStmt = connection.prepareStatement(
                    "SELECT PID, Qty FROM sale_details WHERE ID = ?"
                );
                getDetailsStmt.setInt(1, saleId);
                ResultSet rs = getDetailsStmt.executeQuery();
                
                // Restore product quantities
                PreparedStatement restoreStockStmt = connection.prepareStatement(
                    "UPDATE product SET Sqty = Sqty + ? WHERE PID = ?"
                );
                
                while (rs.next()) {
                    int productId = rs.getInt("PID");
                    int quantity = rs.getInt("Qty");
                    
                    restoreStockStmt.setInt(1, quantity);
                    restoreStockStmt.setInt(2, productId);
                    restoreStockStmt.executeUpdate();
                }
                
                // Delete the sale (details will be deleted via CASCADE)
                PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM sales WHERE ID = ?"
                );
                stmt.setInt(1, saleId);
                int affectedRows = stmt.executeUpdate();
                
                return affectedRows > 0;
            }
        );
    }
    
    /**
     * Search for sales based on a search term.
     * 
     * @param searchTerm The search term
     * @return List of sales with staff information matching the search term
     */
    public List<SaleWithStaff> searchSales(String searchTerm) {
        return DB.unsafeExecute(
            connection -> {
                List<SaleWithStaff> sales = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.ID, s.Date, s.Total, s.SID, st.Name, st.Position, st.UserName, st.Password, st.Role " +
                    "FROM sales s " +
                    "LEFT JOIN staff st ON s.SID = st.ID " +
                    "WHERE st.Name LIKE ? OR st.UserName LIKE ? " +
                    "ORDER BY s.Date DESC"
                );
                
                String pattern = "%" + searchTerm + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Date date;
                    try {
                        date = dateFormat.parse(rs.getString("Date"));
                    } catch (ParseException e) {
                        date = new Date(); // Fallback to current date if parse fails
                    }
                    
                    Sale sale = new Sale(
                        rs.getInt("ID"),
                        date,
                        rs.getBigDecimal("Total"),
                        rs.getInt("SID")
                    );
                    
                    Staff staff = null;
                    if (rs.getInt("SID") > 0) {
                        staff = new Staff(
                            rs.getString("Name"),
                            rs.getString("Position"),
                            rs.getString("UserName"),
                            rs.getString("Password"),
                            rs.getString("Role")
                        );
                    }
                    
                    sales.add(new SaleWithStaff(sale, staff));
                }
                
                return sales;
            }
        );
    }
    
    /**
     * Get sales filtered by date range.
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of sales with staff information in the date range
     */
    public List<SaleWithStaff> getSalesByDateRange(Date startDate, Date endDate) {
        return DB.unsafeExecute(
            connection -> {
                List<SaleWithStaff> sales = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.ID, s.Date, s.Total, s.SID, st.Name, st.Position, st.UserName, st.Password, st.Role " +
                    "FROM sales s " +
                    "LEFT JOIN staff st ON s.SID = st.ID " +
                    "WHERE s.Date BETWEEN ? AND ? " +
                    "ORDER BY s.Date DESC"
                );
                
                stmt.setString(1, dateFormat.format(startDate));
                stmt.setString(2, dateFormat.format(endDate));
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Date date;
                    try {
                        date = dateFormat.parse(rs.getString("Date"));
                    } catch (ParseException e) {
                        date = new Date(); // Fallback to current date if parse fails
                    }
                    
                    Sale sale = new Sale(
                        rs.getInt("ID"),
                        date,
                        rs.getBigDecimal("Total"),
                        rs.getInt("SID")
                    );
                    
                    Staff staff = null;
                    if (rs.getInt("SID") > 0) {
                        staff = new Staff(
                            rs.getString("Name"),
                            rs.getString("Position"),
                            rs.getString("UserName"),
                            rs.getString("Password"),
                            rs.getString("Role")
                        );
                    }
                    
                    sales.add(new SaleWithStaff(sale, staff));
                }
                
                return sales;
            }
        );
    }
} 