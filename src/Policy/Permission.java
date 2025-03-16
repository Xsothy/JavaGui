package Policy;

/**
 * Enum representing different types of permissions that can be granted to users.
 * These permissions can be used to control access to different parts of the application.
 */
public enum Permission {
    READ("read"),
    EDIT("edit"),
    DELETE("delete"),
    BROWSE("browse");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
} 