package Policy;

import Model.Staff;

/**
 * Interface representing a staff member who is authorized to perform a specific action on a specific entity.
 * Similar to the AuthorizedActor interface in the TypeScript code.
 */
public interface AuthorizedActor {
    /**
     * Gets the entity that the actor is authorized to act on.
     *
     * @return The entity
     */
    String getEntity();

    /**
     * Gets the action that the actor is authorized to perform.
     *
     * @return The action
     */
    String getAction();

    /**
     * Gets the underlying staff member.
     *
     * @return The staff member
     */
    Staff getStaff();
} 