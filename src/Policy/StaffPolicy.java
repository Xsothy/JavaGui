package Policy;

import Model.Staff;

import java.util.function.Predicate;

/**
 * Policy class for Staff-related permissions.
 * Similar to the AccountsPolicy class in the TypeScript code.
 */
public class StaffPolicy {
    private static StaffPolicy instance;

    /**
     * Gets the singleton instance of StaffPolicy.
     *
     * @return The StaffPolicy instance
     */
    public static StaffPolicy getInstance() {
        if (instance == null) {
            instance = new StaffPolicy();
        }
        return instance;
    }

    private StaffPolicy() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Creates a predicate that checks if a staff member can read another staff member.
     *
     * @param toRead The ID of the staff member to read
     * @return A predicate that returns true if the actor can read the staff member
     */
    public Predicate<Staff> canRead(int toRead) {
        return actor -> {
            // Admin can read any staff
            if ("admin".equalsIgnoreCase(actor.getRole())) {
                return true;
            }
            
            // Manager can read any staff
            if ("manager".equalsIgnoreCase(actor.getRole())) {
                return true;
            }
            
            // Staff can only read themselves
            return actor.getId() == toRead;
        };
    }

    /**
     * Creates a predicate that checks if a staff member can edit another staff member.
     *
     * @param toEdit The ID of the staff member to edit
     * @return A predicate that returns true if the actor can edit the staff member
     */
    public Predicate<Staff> canEdit(int toEdit) {
        return actor -> {
            // Admin can edit any staff
            if ("admin".equalsIgnoreCase(actor.getRole())) {
                return true;
            }
            
            // Manager can edit non-admin staff
            if ("manager".equalsIgnoreCase(actor.getRole())) {
                // Need to check if toEdit is an admin in a real implementation
                return true;
            }
            
            // Staff can only edit themselves
            return actor.getId() == toEdit;
        };
    }

    /**
     * Creates a predicate that checks if a staff member can delete another staff member.
     *
     * @param toDelete The ID of the staff member to delete
     * @return A predicate that returns true if the actor can delete the staff member
     */
    public Predicate<Staff> canDelete(int toDelete) {
        return actor -> {
            // Only admin can delete staff
            if ("admin".equalsIgnoreCase(actor.getRole())) {
                return true;
            }
            
            // Nobody else can delete staff
            return false;
        };
    }

    /**
     * Creates a predicate that checks if a staff member can browse staff members.
     *
     * @return A predicate that returns true if the actor can browse staff members
     */
    public Predicate<Staff> canBrowse() {
        return actor -> {
            // Admin and manager can browse staff
            return "admin".equalsIgnoreCase(actor.getRole()) || 
                   "manager".equalsIgnoreCase(actor.getRole());
        };
    }
} 