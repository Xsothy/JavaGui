package Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    public int id;
    public String username;
    public String password;
    public String email;
    public String role;

    public User(int id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public boolean checkPassword(String password)
    {
        return password.equals(hashPassword(password));
    }

    public static String hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
