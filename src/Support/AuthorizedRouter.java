package Support;

import Policy.Permission;
import Policy.PermissionChecker;
import Policy.Unauthorized;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Router implementation that checks permissions before navigating.
 */
public class AuthorizedRouter implements RouterInterface {
    private final RouterInterface delegate;
    private final Map<String, RoutePermission> routePermissions;

    /**
     * Creates a new AuthorizedRouter that delegates to another Router.
     *
     * @param delegate The Router to delegate to
     */
    public AuthorizedRouter(RouterInterface delegate) {
        this.delegate = delegate;
        this.routePermissions = new HashMap<>();
    }

    /**
     * Registers a permission check for a route.
     *
     * @param route The route to check
     * @param entity The entity to check permission for
     * @param permission The permission to check
     * @param targetId The ID to check permission for
     * @return This AuthorizedRouter for chaining
     */
    public AuthorizedRouter registerPermission(String route, String entity, Permission permission, int targetId) {
        routePermissions.put(route, new RoutePermission(entity, permission, targetId));
        return this;
    }

    @Override
    public void navigateTo(String route) {
        if (checkPermissionForRoute(route, -1)) {
            delegate.navigateTo(route);
        } else {
            showAccessDenied();
        }
    }

    @Override
    public void navigateTo(String route, String id) {
        try {
            int targetId = Integer.parseInt(id);
            if (checkPermissionForRoute(route, targetId)) {
                delegate.navigateTo(route, id);
            } else {
                showAccessDenied();
            }
        } catch (NumberFormatException e) {
            // If ID is not a number, use the default permission check
            if (checkPermissionForRoute(route, -1)) {
                delegate.navigateTo(route, id);
            } else {
                showAccessDenied();
            }
        }
    }

    @Override
    public void navigateTo(String route, Map<String, String> params) {
        int targetId = -1;
        if (params.containsKey("id")) {
            try {
                targetId = Integer.parseInt(params.get("id"));
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }

        if (checkPermissionForRoute(route, targetId)) {
            delegate.navigateTo(route, params);
        } else {
            showAccessDenied();
        }
    }

    /**
     * Checks if the current user has permission to access a route.
     *
     * @param route The route to check
     * @param targetId The ID to check permission for
     * @return True if the user has permission, false otherwise
     */
    private boolean checkPermissionForRoute(String route, int targetId) {
        RoutePermission permission = routePermissions.get(route);
        if (permission == null) {
            // If no permission is registered for this route, allow access
            return true;
        }

        // Use the target ID from the route permission unless a valid one was provided
        int idToCheck = targetId == -1 ? permission.targetId : targetId;
        
        return PermissionChecker.hasPermission(
                permission.entity,
                permission.permission,
                idToCheck
        );
    }

    /**
     * Shows an access denied message.
     */
    private void showAccessDenied() {
        JOptionPane.showMessageDialog(
                null,
                "You don't have permission to access this resource.",
                "Access Denied",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Class that represents a permission check for a route.
     */
    private static class RoutePermission {
        private final String entity;
        private final Permission permission;
        private final int targetId;

        /**
         * Creates a new RoutePermission.
         *
         * @param entity The entity to check permission for
         * @param permission The permission to check
         * @param targetId The ID to check permission for
         */
        public RoutePermission(String entity, Permission permission, int targetId) {
            this.entity = entity;
            this.permission = permission;
            this.targetId = targetId;
        }
    }

    @Override
    public JPanel getContainer() {
        return delegate.getContainer();
    }
} 