# Policy and Authorization System

This directory contains classes for managing user permissions and authorization in the application.

## Overview

The policy system provides a way to check if a user is authorized to perform actions on entities. It can be used to:

1. Hide UI components based on permissions
2. Block unauthorized access to routes
3. Throw exceptions when unauthorized actions are attempted

## Key Components

- `Permission`: Enum defining the types of permissions (READ, EDIT, DELETE, BROWSE)
- `Unauthorized`: Exception thrown when a user is not authorized
- `AuthorizedActor`: Interface for a user who is authorized to perform an action
- `AuthorizedStaff`: Implementation of AuthorizedActor for Staff
- `PolicyService`: Service for checking authorization
- `PermissionChecker`: Utility for checking permissions and hiding UI components
- `StaffPolicy`: Policy class with specific rules for Staff entities
- `AuthorizedRouter`: Router implementation that checks permissions before navigation

## How to Use

### 1. Hiding UI Components Based on Permissions

```java
// Hide the edit button if the user doesn't have edit permission
PermissionChecker.hideIfUnauthorized(editButton, "Staff", Permission.EDIT, staffId);

// Hide the delete button if the user doesn't have delete permission
PermissionChecker.hideIfUnauthorized(deleteButton, "Staff", Permission.DELETE, staffId);
```

### 2. Checking Permissions in Code

```java
try {
    // Attempt to get an authorized actor for editing a staff member
    AuthorizedActor actor = PolicyService.getInstance().canEdit("Staff", 
        staff -> StaffPolicy.getInstance().canEdit(staffId).test(staff));
    
    // If we get here, the user is authorized
    // The actor contains the authorized staff member
    Staff authorizedStaff = actor.getStaff();
    
    // Perform the edit operation
    // ...
} catch (Unauthorized e) {
    // User is not authorized, show an error message
    JOptionPane.showMessageDialog(null, e.getMessage(), "Access Denied", JOptionPane.ERROR_MESSAGE);
}
```

### 3. Route Authorization

```java
// Create an AuthorizedRouter that delegates to the regular router
Router baseRouter = /* get the base router */;
AuthorizedRouter router = new AuthorizedRouter(baseRouter);

// Register permissions for routes
router.registerPermission("staff/edit", "Staff", Permission.EDIT, -1)
      .registerPermission("staff/delete", "Staff", Permission.DELETE, -1)
      .registerPermission("staff/list", "Staff", Permission.BROWSE, -1);

// Use the router for navigation
// The router will check permissions before navigating
router.navigateTo("staff/edit", "123");  // Only works if user can edit staff #123
```

### 4. Login Integration

When a user logs in, set the current user in the SessionManager:

```java
// In your login logic
Staff user = /* get the authenticated user */;
SessionManager.setCurrentUser(user);
```

When a user logs out, clear the current user:

```java
// In your logout logic
SessionManager.logout();
```

## Entity and Permission Naming

For consistency, use the following entity names:

- "Staff" for staff-related operations
- "Expense" for expense-related operations

And use the standard Permission enum values:

- Permission.READ for viewing details
- Permission.EDIT for editing
- Permission.DELETE for deleting
- Permission.BROWSE for listing/browsing

## Policy Rules Example

Here's how the StaffPolicy implements rules for who can access staff data:

```java
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
``` 