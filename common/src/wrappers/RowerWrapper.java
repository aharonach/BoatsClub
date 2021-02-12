package wrappers;

import entities.Entity;
import entities.Rower;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RowerWrapper implements Wrapper, Serializable {
    private final String name;
    private final Integer age;
    private final LocalDateTime expired;
    private final Rower.Level level;
    private final Boolean hasPrivateBoat;
    private final Integer privateBoat;
    private final String phoneNumber;
    private final String notes;
    private final Boolean isManager;
    private final String emailAddress;
    private final String password;
    private final LocalDateTime joined;
    private final int id;

    public RowerWrapper(int id, String name, Integer age, LocalDateTime expired, Rower.Level level,
                        Boolean hasPrivateBoat, Integer privateBoat, String phoneNumber, String notes, Boolean isManager
            , String emailAddress, String password, LocalDateTime joined) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.expired = expired;
        this.level = level;
        this.hasPrivateBoat = hasPrivateBoat;
        this.privateBoat = privateBoat;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
        this.isManager = isManager;
        this.emailAddress = emailAddress;
        this.password = password;
        this.joined = joined;
    }

    public RowerWrapper(Rower rower) {
        this.id = rower.getId();
        this.name = rower.getName();
        this.age = rower.getAge();
        this.expired = rower.getExpired();
        this.level = rower.getLevel();
        this.hasPrivateBoat = rower.hasPrivateBoat();
        this.privateBoat = rower.getPrivateBoat();
        this.phoneNumber = rower.getPhoneNumber();
        this.notes = rower.getNotes();
        this.isManager = rower.isManager();
        this.emailAddress = rower.getEmailAddress();
        this.password = rower.getPassword();
        this.joined = rower.getJoined();
    }

    public static RowerWrapper create(Entity entity) {
        return new RowerWrapper((Rower) entity);
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDateTime getExpired() {
        return expired;
    }

    public Rower.Level getLevel() {
        return level;
    }

    public Boolean hasPrivateBoat() {
        return hasPrivateBoat;
    }

    public Integer getPrivateBoat() {
        return privateBoat;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public Boolean isManager() {
        return isManager;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getJoined() {
        return joined;
    }

    @Override
    public Map<String, Object> updatedFields() {
        Map<String, Object> fields = new HashMap<>();
        if (null != getName()) {
            fields.put("name", getName());
        }
        if (null != getAge()) {
            fields.put("age", getAge());
        }
        if (null != getExpired()) {
            fields.put("expired", getExpired());
        }
        if (null != getLevel()) {
            fields.put("level", getLevel());
        }
        if (null != hasPrivateBoat()) {
            fields.put("privateBoat", hasPrivateBoat());
        }
        if (null != getPrivateBoat()) {
            fields.put("boat", getPrivateBoat());
        }
        if (null != getPhoneNumber()) {
            fields.put("phoneNumber", getPhoneNumber());
        }
        if (null != getNotes()) {
            fields.put("notes", getNotes());
        }
        if (null != isManager()) {
            fields.put("manager", isManager());
        }
        if (null != getEmailAddress()) {
            fields.put("email", getEmailAddress());
        }
        if (null != getPassword()) {
            fields.put("password", getPassword());
        }
        if (null != getJoined()) {
            fields.put("joined", getJoined());
        }
        return fields;
    }
}
