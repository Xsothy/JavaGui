package Policy;

import Model.Staff;

/**
 * Implementation of AuthorizedActor that wraps a Staff member and provides authorization information.
 */
public class AuthorizedStaff implements AuthorizedActor {
    private final Staff staff;
    private final String entity;
    private final String action;

    /**
     * Creates a new AuthorizedStaff.
     *
     * @param staff The staff member
     * @param entity The entity that the staff member is authorized to act on
     * @param action The action that the staff member is authorized to perform
     */
    public AuthorizedStaff(Staff staff, String entity, String action) {
        this.staff = staff;
        this.entity = entity;
        this.action = action;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public Staff getStaff() {
        return staff;
    }

    /**
     * Creates a new AuthorizedStaff with the same staff member but different entity and action.
     *
     * @param entity The new entity
     * @param action The new action
     * @return The new AuthorizedStaff
     */
    public AuthorizedStaff withPermission(String entity, String action) {
        return new AuthorizedStaff(this.staff, entity, action);
    }
} 