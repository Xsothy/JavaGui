package Policy;

import Model.Staff;
import Support.SessionManager;

import javax.swing.JComponent;

/**
 * Utility class for checking permissions and applying UI changes based on permissions.
 */
public class PermissionChecker {
    private static final PolicyService policyService = PolicyService.getInstance();

    /**
     * Hides a component if the current user doesn't have permission.
     *
     * @param component The component to hide
     * @param entity The entity to check permission for
     * @param permission The permission to check
     * @param targetId The ID to check permission for (e.g., staff ID)
     */
    public static void hideIfUnauthorized(JComponent component, String entity, Permission permission, int targetId) {
        try {
            Staff currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) {
                component.setVisible(false);
                return;
            }

            switch (permission) {
                case READ:
                    policyService.canRead(entity, staff -> checkPermission(staff, targetId));
                    break;
                case EDIT:
                    policyService.canEdit(entity, staff -> checkPermission(staff, targetId));
                    break;
                case DELETE:
                    policyService.canDelete(entity, staff -> checkPermission(staff, targetId));
                    break;
                case BROWSE:
                    policyService.canBrowse(entity, staff -> checkPermission(staff, targetId));
                    break;
            }
            
            // If we get here, the user is authorized
            component.setVisible(true);
        } catch (Unauthorized e) {
            // User is not authorized, hide the component
            component.setVisible(false);
        }
    }

    /**
     * Checks if the current user has permission for an action on an entity.
     *
     * @param entity The entity to check
     * @param permission The permission to check
     * @param targetId The ID to check permission for
     * @return True if the user has permission, false otherwise
     */
    public static boolean hasPermission(String entity, Permission permission, int targetId) {
        try {
            Staff currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) {
                return false;
            }

            switch (permission) {
                case READ:
                    policyService.canRead(entity, staff -> checkPermission(staff, targetId));
                    break;
                case EDIT:
                    policyService.canEdit(entity, staff -> checkPermission(staff, targetId));
                    break;
                case DELETE:
                    policyService.canDelete(entity, staff -> checkPermission(staff, targetId));
                    break;
                case BROWSE:
                    policyService.canBrowse(entity, staff -> checkPermission(staff, targetId));
                    break;
            }
            
            // If we get here, the user is authorized
            return true;
        } catch (Unauthorized e) {
            // User is not authorized
            return false;
        }
    }

    /**
     * Default permission check logic.
     * Can be overridden in subclasses or specific entity policies.
     *
     * @param staff The staff member
     * @param targetId The ID to check permission for
     * @return True if the staff member has permission, false otherwise
     */
    private static boolean checkPermission(Staff staff, int targetId) {
        // Admin can do anything
        if ("admin".equalsIgnoreCase(staff.getRole())) {
            return true;
        }
        
        // Users can only access their own data
        return staff.getId() == targetId;
    }
} 