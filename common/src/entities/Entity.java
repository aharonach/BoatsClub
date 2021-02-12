package entities;

import java.io.Serializable;

public abstract class Entity implements Cloneable, Serializable {
    protected static String entityType;
    /**
     * The Id of the entity
     */
    protected int id;

    public Entity() {
        entityType = this.getEntityType();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Should be implemented by extending class
     *
     * @return record type
     */
    abstract public String getEntityType();

    abstract public void updateField(String field, Object value);
}
