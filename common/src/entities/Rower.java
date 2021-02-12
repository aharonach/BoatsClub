package entities;

import wrappers.RowerWrapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Rower extends Entity implements Cloneable, Serializable {
    public static final int yearsUntilExpired = 4;
    private static int idCounter = 0;

    private String name;
    private Integer age;
    private LocalDateTime expired;
    private Level level;
    private boolean hasPrivateBoat;
    private Integer privateBoat;
    private String phoneNumber;
    private String notes;
    private boolean isManager;
    private String emailAddress;
    private String password;
    private LocalDateTime joined;

    public Rower(RowerWrapper rower) {
        this.setId(++idCounter);
        this.setName(rower.getName());
        this.setLevel(rower.getLevel());
        this.setPhoneNumber(rower.getPhoneNumber());
        this.setEmailAddress(rower.getEmailAddress());
        this.setAge(rower.getAge());
        this.setJoined(rower.getJoined());
        this.setExpired(rower.getExpired());
        this.setPassword(rower.getPassword());
        this.setIsManager(rower.isManager());
        this.setHasPrivateBoat(rower.hasPrivateBoat());
        this.setNotes(rower.getNotes());
    }

    public Rower(String name, Level level, Integer age, LocalDateTime joined, LocalDateTime expired, String phoneNumber,
                 String emailAddress,
                 String password, boolean hasPrivateBoat, boolean isManager, String notes) {
        this.setId(++idCounter);
        this.setName(name);
        this.setLevel(level);
        this.setPhoneNumber(phoneNumber);
        this.setEmailAddress(emailAddress);
        this.setAge(age);
        this.setJoined(joined);
        this.setExpired(expired);
        this.setPassword(password);
        this.setIsManager(isManager);
        this.setHasPrivateBoat(hasPrivateBoat);
        this.setNotes(notes);
    }

    @Override
    public String getEntityType() {
        return "rowers";
    }

    public void updateField(String field, Object value) {
        switch (field) {
            case "name":
                this.setName((String) value);
                break;
            case "age":
                this.setAge((Integer) value);
                break;
            case "expired":
                this.setExpired((LocalDateTime) value);
                break;
            case "level":
                this.setLevel((Level) value);
                break;
            case "privateBoat":
                this.setHasPrivateBoat((boolean) value);
                break;
            case "boat":
                this.setPrivateBoat((Integer) value);
                break;
            case "phoneNumber":
                this.setPhoneNumber((String) value);
                break;
            case "notes":
                this.setNotes((String) value);
                break;
            case "manager":
                this.setIsManager((Boolean) value);
                break;
            case "email":
                this.setEmailAddress((String) value);
                break;
            case "password":
                this.setPassword((String) value);
                break;
            case "joined":
                this.setJoined((LocalDateTime) value);
                break;
            default:
        }
    }

    //getters
    public String getName() {
        return name;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean hasPrivateBoat() {
        return hasPrivateBoat;
    }

    public LocalDateTime getExpired() {
        return expired;
    }

    public void setExpired(LocalDateTime expired) {
        this.expired = expired;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPrivateBoat() {
        return privateBoat;
    }

    public void setPrivateBoat(Integer privateBoat) {
        this.privateBoat = privateBoat;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isManager() {
        return isManager;
    }

    public LocalDateTime getJoined() {
        return joined;
    }

    public void setJoined(LocalDateTime joined) {
        this.joined = joined;
    }

    public void setHasPrivateBoat(boolean hasPrivateBoat) {
        this.hasPrivateBoat = hasPrivateBoat;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public String toString() {
        return (this.getName() + ", " + this.getId() + ", " + this.getEmailAddress() + ", " + level + (this.isManager() ? ", " + "Manager" : ""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rower user = (Rower) o;
        return emailAddress.equals(user.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }

    public enum Level {
        BEGINNER("Beginner"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced");

        private final String name;

        Level(String name) {
            this.name = name;
        }

        public static String[] getLevels() {
            String[] values = new String[Level.values().length];
            for (int i = 0; i < values.length; i++) {
                values[i] = Level.values()[i].getName();
            }
            return values;
        }

        public static String[] getLevelsWithIndexes() {
            String[] values = getLevels();
            for (int i = 0; i < values.length; i++) {
                values[i] = i + "- " + values[i];
            }
            return values;
        }

        public static Level getLevelByIndex(int index) {
            return Level.values()[index];
        }

        public static int count() {
            return Level.values().length;
        }

        public String getName() {
            return this.name;
        }
    }

}


