package Support;

import Model.Staff;

/**
 * Manages the current user session.
 */
public class SessionManager {
    private static Staff currentUser;

    /**
     * Gets the current user.
     *
     * @return The current user, or null if no user is logged in
     */
    public static Staff getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user.
     *
     * @param staff The staff member to set as the current user
     */
    public static void setCurrentUser(Staff staff) {
        currentUser = staff;
    }

    /**
     * Clears the current user session.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
} 