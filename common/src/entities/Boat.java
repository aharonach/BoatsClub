package entities;

import wrappers.BoatWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Boat extends Entity implements Serializable {
    private static int idCounter = 0;

    private Type type;
    private String name;
    private String typeName;
    private boolean isPrivate = false;
    private boolean isWide = false;
    private boolean isCoastal = false;
    private boolean isDisabled = false;

    /**
     * Constructor with all properties
     *
     * @param name       boat name
     * @param type       boat type
     * @param isPrivate  is private
     * @param isWide     is wide
     * @param isCoastal  is coastal
     * @param isDisabled is disabled
     */
    public Boat(Type type, String name, boolean isPrivate, boolean isWide, boolean isCoastal,
                boolean isDisabled) {
        this.setId(++idCounter);
        this.setType(type);
        this.setName(name);
        this.setPrivate(isPrivate);
        this.setWide(isWide);
        this.setCoastal(isCoastal);
        this.setDisabled(isDisabled);
        this.typeName = getTypeShortName();
    }

    /**
     * Create new boat with name only
     * All other boolean fields will be false
     *
     * @param name boat name
     * @param type boat type
     */
    public Boat(Type type, String name) {
        this.setId(++idCounter);
        this.setType(type);
        this.setName(name);
        this.typeName = getTypeShortName();
    }

    public Boat(BoatWrapper newBoat) {
        this.setId(++idCounter);
        this.setType(newBoat.getType());
        this.setName(newBoat.getName());
        this.setPrivate(newBoat.isPrivate());
        this.setWide(newBoat.isWide());
        this.setCoastal(newBoat.isCoastal());
        this.setDisabled(newBoat.isDisabled());
        this.typeName = getTypeShortName();
    }

    @Override
    public String getEntityType() {
        return "boats";
    }

    @Override
    public void updateField(String field, Object value) {
        switch (field) {
            case "type":
                this.setType((Type) value);
                break;
            case "name":
                this.setName((String) value);
                break;
            case "coastal":
                this.setCoastal((Boolean) value);
                break;
            case "private":
                this.setPrivate((Boolean) value);
                break;
            case "wide":
                this.setWide((Boolean) value);
                break;
            default:
        }
    }

    /**
     * Get Boat name
     *
     * @return boat name
     */
    public String getName() {
        return name;
    }

    /**
     * Set Boat name
     *
     * @param name boat name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get boat type
     *
     * @return Type type
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        this.typeName = getTypeShortName();
    }

    /**
     * Get boat type - short name
     */
    public String getTypeShortName() {
        return this.getType().getShortName(this.isWide(), this.isCoastal());
    }

    /**
     * Is the boat private
     *
     * @return boolean
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Set boat privacy
     *
     * @param aPrivate boat privacy status
     */
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    /**
     * Is the boat a wide boat
     *
     * @return boolean
     */
    public boolean isWide() {
        return isWide;
    }

    /**
     * Set boat wide
     *
     * @param wide is the boat wide
     */
    public void setWide(boolean wide) {
        isWide = wide;
        this.typeName = getTypeShortName();
    }

    /**
     * The boat has coxswain
     *
     * @return boolean boat has coxswain
     */
    public boolean hasCoxswain() {
        return getType().hasCoxswain();
    }

    /**
     * Is the boat is a sea boat
     *
     * @return boolean
     */
    public boolean isCoastal() {
        return isCoastal;
    }

    /**
     * Set sea boat property
     *
     * @param coastal is the boat coastal
     */
    public void setCoastal(boolean coastal) {
        isCoastal = coastal;
        this.typeName = getTypeShortName();
    }

    /**
     * Is the boat disabled
     *
     * @return boolean
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Set boat disability
     *
     * @param disabled is the boat disabled
     */
    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    /**
     * Get max capacity of the boat
     *
     * @return int
     */
    public int getMaxCapacity() {
        return this.getType().getMaxCapacity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boat boat = (Boat) o;
        return isPrivate == boat.isPrivate && isWide == boat.isWide && isCoastal == boat.isCoastal && isDisabled == boat.isDisabled && type == boat.type && name.equals(boat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, isPrivate, isWide, isCoastal, isDisabled);
    }

    public enum Type {
        Single("Single", "1X", 1),
        Double("Double", "2X", 2),
        Coxed_Double("Coxed Double", "2X+", 2),
        Pair("Pair", "2-", 2),
        Coxed_Pair("Coxed Pair", "2+", 2),
        Four("Four", "4-", 4),
        Coxed_Four("Coxed Four", "4+", 4),
        Quad("Quad", "4X-", 4),
        Coxed_Quad("Coxed Quad", "4x+", 4),
        Octuple("Octuple", "8X+", 8),
        Eight("Eight", "8+", 8);

        private final String name;
        private final String shortName;
        private final int maxCapacity;

        Type(String name, String shortName, int maxCapacity) {
            this.name = name;
            this.shortName = shortName;
            this.maxCapacity = maxCapacity;
        }

        public static String[] getTypes() {
            String[] values = new String[Type.values().length];
            for (int i = 0; i < values.length; i++) {
                values[i] = Type.values()[i].getName();
            }
            return values;
        }

        public static String[] getTypesWithIndexes() {
            String[] values = getTypes();
            for (int i = 0; i < values.length; i++) {
                values[i] = i + "- " + values[i];
            }
            return values;
        }

        public static Type getTypeByIndex(int index) {
            return Type.values()[index];
        }

        public static Type[] getTypesSameCapacity(Type type) {
            List<Type> list = new ArrayList<>();
            for (Type t : Type.values()) {
                if (t.getMaxCapacity() == type.getMaxCapacity()) {
                    list.add(t);
                }
            }
            return list.toArray(new Type[0]);
        }

        public static String[] getTypesNames(Type[] types) {
            return (String[]) Arrays.stream(types).map(Boat.Type::getName).toArray();
        }

        public static int count() {
            return Type.values().length;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return this.shortName;
        }

        public String getShortName(boolean isWide, boolean isCoastal) {
            String shortName = this.shortName;
            if (isWide) {
                shortName = shortName.concat(" wide");
            }
            if (isCoastal) {
                shortName = shortName.concat(" coastal");
            }
            return shortName;
        }

        public boolean isOneOar() {
            return this.shortName.contains("-");
        }

        public boolean hasCoxswain() {
            return this.shortName.contains("+");
        }

        public boolean isSingle() {
            return this.shortName.contains("1");
        }

        public boolean isDouble() {
            return this.shortName.contains("2");
        }

        public boolean isQuartet() {
            return this.shortName.contains("4");
        }

        public boolean isOctet() {
            return this.shortName.contains("8");
        }

        public int getMaxCapacity() {
            return this.maxCapacity;
        }
    }
}
