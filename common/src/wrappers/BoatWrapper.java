package wrappers;

import entities.Boat;
import entities.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoatWrapper implements Wrapper, Serializable {
    private final Boat.Type type;
    private final String name;
    private final Boolean isPrivate;
    private final Boolean isWide;
    private final Boolean isCoastal;
    private final Boolean isDisabled;
    private final int id;

    /**
     * Construct with properties
     */
    public BoatWrapper(int id, String name, Boat.Type type, Boolean isPrivate, Boolean isWide, Boolean isCoastal,
                       Boolean isDisabled) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.isPrivate = isPrivate;
        this.isWide = isWide;
        this.isCoastal = isCoastal;
        this.isDisabled = isDisabled;
    }

    /**
     * Construct from Boat object
     *
     * @param boat boat to copy from
     */
    public BoatWrapper(Boat boat) {
        this.id = boat.getId();
        this.name = boat.getName();
        this.type = boat.getType();
        this.isPrivate = boat.isPrivate();
        this.isWide = boat.isWide();
        this.isCoastal = boat.isCoastal();
        this.isDisabled = boat.isDisabled();
    }

    public static BoatWrapper create(Entity entity) {
        return new BoatWrapper((Boat) entity);
    }

    public int getId() {
        return this.id;
    }

    public Boat.Type getType() {
        return type;
    }

    /**
     * Get boat type - short name
     */
    public String getTypeShortName() {
        return this.getType().getShortName(this.isWide(), this.isCoastal());
    }

    public String getName() {
        return name;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public Boolean isWide() {
        return isWide;
    }

    public Boolean isCoastal() {
        return isCoastal;
    }

    public Boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public Map<String, Object> updatedFields() {
        Map<String, Object> fields = new HashMap<>();
        if (null != getType()) {
            fields.put("type", getType());
        }
        if (null != getName()) {
            fields.put("name", getName());
        }
        if (null != isPrivate()) {
            fields.put("private", isPrivate());
        }
        if (null != isWide()) {
            fields.put("wide", isWide());
        }
        if (null != isCoastal()) {
            fields.put("coastal", isCoastal());
        }
        if (null != isDisabled()) {
            fields.put("disabled", isDisabled());
        }
        return fields;
    }
}
