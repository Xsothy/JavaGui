package Policy;

import Model.Staff;
import Support.SessionManager;

import java.util.function.Predicate;

/**
 * Service for checking if a staff member is authorized to perform an action on an entity.
 * This is similar to the policies in the TypeScript code.
 */
public class PolicyService {
    private static PolicyService instance;

    /**
     * Gets the singleton instance of PolicyService.
     *
     * @return The PolicyService instance
     */
    public static PolicyService getInstance() {
        if (instance == null) {
            instance = new PolicyService();
        }
        return instance;
    }

    private PolicyService() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Checks if the current user is authorized to perform an action on an entity.
     *
     * @param entity The entity to act on
     * @param action The action to perform
     * @param authorizationCheck A predicate that checks if the staff member is authorized
     * @return An AuthorizedActor if the staff member is authorized, otherwise throws Unauthorized
     * @throws Unauthorized If the staff member is not authorized
     */
    public AuthorizedActor authorize(String entity, String action, Predicate<Staff> authorizationCheck) throws Unauthorized {
        Staff currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            throw new Unauthorized(-1, entity, action);
        }

        if (authorizationCheck.test(currentUser)) {
            return new AuthorizedStaff(currentUser, entity, action);
        } else {
            throw Unauthorized.fromStaff(currentUser, entity, action);
        }
    }

    /**
     * Checks if the current user can read the specified entity.
     *
     * @param entity The entity to read
     * @param predicate A predicate that checks if the staff member can read the entity
     * @return An AuthorizedActor if the staff member can read, otherwise throws Unauthorized
     * @throws Unauthorized If the staff member is not authorized
     */
    public AuthorizedActor canRead(String entity, Predicate<Staff> predicate) throws Unauthorized {
        return authorize(entity, Permission.READ.getValue(), predicate);
    }

    /**
     * Checks if the current user can edit the specified entity.
     *
     * @param entity The entity to edit
     * @param predicate A predicate that checks if the staff member can edit the entity
     * @return An AuthorizedActor if the staff member can edit, otherwise throws Unauthorized
     * @throws Unauthorized If the staff member is not authorized
     */
    public AuthorizedActor canEdit(String entity, Predicate<Staff> predicate) throws Unauthorized {
        return authorize(entity, Permission.EDIT.getValue(), predicate);
    }

    /**
     * Checks if the current user can delete the specified entity.
     *
     * @param entity The entity to delete
     * @param predicate A predicate that checks if the staff member can delete the entity
     * @return An AuthorizedActor if the staff member can delete, otherwise throws Unauthorized
     * @throws Unauthorized If the staff member is not authorized
     */
    public AuthorizedActor canDelete(String entity, Predicate<Staff> predicate) throws Unauthorized {
        return authorize(entity, Permission.DELETE.getValue(), predicate);
    }

    /**
     * Checks if the current user can browse the specified entity.
     *
     * @param entity The entity to browse
     * @param predicate A predicate that checks if the staff member can browse the entity
     * @return An AuthorizedActor if the staff member can browse, otherwise throws Unauthorized
     * @throws Unauthorized If the staff member is not authorized
     */
    public AuthorizedActor canBrowse(String entity, Predicate<Staff> predicate) throws Unauthorized {
        return authorize(entity, Permission.BROWSE.getValue(), predicate);
    }
} 