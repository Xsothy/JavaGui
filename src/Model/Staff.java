package Model;

import java.util.Objects;

public class Staff {
    private int id;
    private String name;
    private String position;
    private String userName;
    private String password;
    private String role;

    public Staff(int id, String name, String position, String userName, String password, String role) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Constructor for creating a new Staff (without ID)
    public Staff(String name, String position, String userName, String password, String role) {
        this(-1, name, position, userName, password, role);
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Static method to hash passwords
    public static String hashPassword(String password) {
        // In a real app, use a secure hashing algorithm like BCrypt
        // This is a simple example (not secure for production)
        return String.valueOf(Objects.hash(password));
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}