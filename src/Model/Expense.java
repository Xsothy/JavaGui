package Model;

import java.math.BigDecimal;

public class Expense {
    private int id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String picture;
    private int staffId;

    public Expense(int id, String name, String description, BigDecimal amount, String picture, int staffId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.picture = picture;
        this.staffId = staffId;
    }

    // Constructor for creating a new Expense (without ID)
    public Expense(String name, String description, BigDecimal amount, String picture, int staffId) {
        this(-1, name, description, amount, picture, staffId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getStaffId() {
        return staffId;
    }

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