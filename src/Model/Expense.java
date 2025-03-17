package Model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Model class representing an expense entry.
 */
public class Expense {
    private int id;
    private Date date;
    private String name;
    private String description;
    private BigDecimal amount;
    private String picture;
    private int staffId;

    /**
     * Constructor for Expense.
     *
     * @param id The expense ID
     * @param name The name of the expense
     * @param description The expense description
     * @param amount The expense amount
     * @param picture Path to the expense receipt/picture (if any)
     * @param staffId The ID of the staff member associated with this expense
     */
    public Expense(int id, Date date, String name, String description, BigDecimal amount, String picture, int staffId) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.picture = picture;
        this.staffId = staffId;
    }

    /**
     * Get the expense ID.
     * 
     * @return The expense ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set the expense ID.
     * 
     * @param id The new expense ID
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Get the expense ID.
     *
     * @return The expense ID
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the expense ID.
     *
     * @param date The new expense ID
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the expense name.
     * 
     * @return The expense name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the expense name.
     * 
     * @param name The new expense name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the expense description.
     * 
     * @return The expense description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the expense description.
     * 
     * @param description The new expense description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the expense amount.
     * 
     * @return The expense amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set the expense amount.
     * 
     * @param amount The new expense amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Get the path to the expense receipt/picture.
     * 
     * @return The picture path
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Set the path to the expense receipt/picture.
     * 
     * @param picture The new picture path
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Get the ID of the staff member associated with this expense.
     * 
     * @return The staff ID
     */
    public int getStaffId() {
        return staffId;
    }

    /**
     * Set the ID of the staff member associated with this expense.
     * 
     * @param staffId The new staff ID
     */
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", picture='" + picture + '\'' +
                ", staffId=" + staffId +
                '}';
    }
}