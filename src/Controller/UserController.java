package Controller;

import Model.User;
import Repository.UserRepository;
import Support.Response;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;


public class UserController {
    private static final UserRepository userRepository = new UserRepository();

    public static Response<User> login(String username, String password) {
        if (username.isEmpty()) {
            return Response.error("username required");
        }

        if (password.isEmpty()) {
            return Response.error("password required");
        }

        Response<Optional<User>> userResponse = userRepository
                .getUserByUsername(username);

        if (!userResponse.isSuccess()) {
            return Response.error("Error accessing user data > " + userResponse.getMessage());
        }

        Optional<User> optionalUser = userResponse.getData();

        if (optionalUser.isEmpty()) {
            return Response.error("Invalid username!");
        }

        User user = optionalUser.get();

        if (!user.checkPassword(password)) {
            return Response.error("Invalid password!");
        }

        return Response.success("Login Successful!", user);
    }

    public static void register(String username, String password, String email, String role) {
        if (username.isEmpty()) {
            return;
        }

        if (password.isEmpty()) {
            return;
        }

        if (email.isEmpty()) {
            return;
        }

        if (role.isEmpty()) {
            return;
        }

        if (
                userRepository.getUserByUsername(username)
                        .getData()
                        .isPresent()
        ) {
            return;
        }

        userRepository.createUser(username, password, email, role);
    }

    public static Response<String> updateProfile(String username, String password, String email, String role) {
        return Response.success("Update Success!", null);
    }
}
