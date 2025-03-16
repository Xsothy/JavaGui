package Policy;

import Model.Staff;

/**
 * Exception thrown when a user is not authorized to perform an action on an entity.
 * Similar to the Unauthorized class in the TypeScript code.
 */
public class Unauthorized extends Exception {
    private final int actorId;
    private final String entity;
    private final String action;

    /**
     * Creates a new Unauthorized exception.
     *
     * @param actorId The ID of the actor who tried to perform the action
     * @param entity The entity on which the action was attempted
     * @param action The action that was attempted
     */
    public Unauthorized(int actorId, String entity, String action) {
        super(String.format("Actor (%d) is not authorized to perform action \"%s\" on entity \"%s\"", 
                actorId, action, entity));
        this.actorId = actorId;
        this.entity = entity;
        this.action = action;
    }

    /**
     * Creates a new Unauthorized exception from a staff member.
     *
     * @param actor The staff member who tried to perform the action
     * @param entity The entity on which the action was attempted
     * @param action The action that was attempted
     * @return The Unauthorized exception
     */
    public static Unauthorized fromStaff(Staff actor, String entity, String action) {
        return new Unauthorized(actor.getId(), entity, action);
    }

    /**
     * Gets the ID of the actor who tried to perform the action.
     *
     * @return The actor ID
     */
    public int getActorId() {
        return actorId;
    }

    /**
     * Gets the entity on which the action was attempted.
     *
     * @return The entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Gets the action that was attempted.
     *
     * @return The action
     */
    public String getAction() {
        return action;
    }
} 