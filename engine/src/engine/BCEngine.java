package engine;

import data.*;
import encryption.BCrypt;
import entities.Entity;
import entities.Rower;
import interfaces.Controller;
import interfaces.Engine;
import sun.security.util.Password;
import utils.Utils;
import exceptions.AlreadyLoggedInException;
import exceptions.RecordNotFoundException;

import javax.security.auth.login.CredentialNotFoundException;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BCEngine implements Engine {
    /**
     * Engine instance for singleton
     */
    private static final BCEngine self = new BCEngine();

    /**
     * Instances of controllers
     */
    private final Map<String, Controller> controllers;

    /**
     * Database instance
     */
    private final Database database;

    /**
     * XML Database
     */
    private final XMLDatabase xmlDatabase;

    /**
     * Notifications
     */
    private final Notifications notifications;

    /**
     * Current logged in user
     */
    private Rower user;

    /**
     * Private constructor.
     * Don't allow external users to create a new instance of the engine.
     */
    private BCEngine() {
        this.database = new Database();
        this.xmlDatabase = new XMLDatabase(this.database);
        this.notifications = new Notifications(database.get("rowers").values().toArray(new Rower[0]));

        // Init controllers
        controllers = new HashMap<>(Database.getEntityTypes().length);

        for (String entityType : Database.getEntityTypes()) {
            try {
                controllers.put(entityType, (Controller) Class.forName("controllers." + Utils.capitalize(entityType)).newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | ExceptionInInitializerError ignored) {
                // catch ignored
            }
        }

        // create first admin if no admin rowers exists
        if (filterList( "rowers", r -> ((Rower) r).isManager() ).size() == 0) {
            createAdmin();
        }
    }

    /**
     * Singleton implementation
     *
     * @return engine instance
     */
    public static BCEngine instance() {
        return self;
    }

    /**
     * Get a list of records of an entity from database
     *
     * @param entityType Entity Type
     * @return Map of records of an entity
     */
    public Map<Integer, Entity> getList(String entityType) {
        return database.get(entityType);
    }

    public Map<Integer, Entity> filterList(String entityType, Predicate<? super Entity> condition) {
        return getList(entityType).values().stream().filter(condition).collect(Collectors.toMap(Entity::getId, v -> v));
    }

    public Controller getController(String entityType) {
        return controllers.get(entityType);
    }

    /**
     * Get a record from database
     * @param entityType type to get
     * @param id of record
     * @return entity record
     * @throws RecordNotFoundException if record not found
     */
    public Entity getRecord(String entityType, int id) throws RecordNotFoundException {
        Entity entity = database.get(entityType).get(id);
        if (entity == null) {
            throw new RecordNotFoundException(entityType, id);
        }
        return entity;
    }

    /**
     * Add record to database
     * @param entityType type to add
     * @param record to add
     */
    public void addRecord(String entityType, Entity record) {
        getList(entityType).put(record.getId(), record);
        getXML().addRecordElement(record.getId(), record);
    }

    /**
     * Update a record in the database
     * @param entityType type to update
     * @param record to update
     */
    public void updateRecord(String entityType, Entity record) {
        getList(entityType).replace(record.getId(), record);
        getXML().updateRecordElement(record.getId(), record);
    }

    /**
     * Delete a record from the database
     * @param entityType type to delete
     * @param record to delete
     */
    public void deleteRecord(String entityType, Entity record) {
        getList(entityType).remove(record.getId());
        getXML().deleteRecordElement(record.getId(), record);
    }

    /**
     * @return logged in user
     */
    public Rower getUser() {
        return user;
    }

    public void setUser(Integer rower) {
        Rower user = null;
        if (rower != null) {
            try {
                user = (Rower) getRecord("rowers", rower);
                ActiveUsers.addActiveUser(user.getId());
            } catch (RecordNotFoundException ignored) {}
        }
        this.user = user;
    }

    /**
     * Check if user logged in
     *
     * @return boolean true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return this.getUser() != null;
    }

    /**
     * Check if current user is an admin
     *
     * @return boolean
     */
    public boolean isAdmin() {
        return isLoggedIn() && this.getUser().isManager();
    }

    @Override
    public Rower authenticate(String email, String password) throws CredentialNotFoundException {
        for (Entity entry : getList("rowers").values()) {
            Rower rower = (Rower) entry;
            if (rower.getEmailAddress().equalsIgnoreCase(email)) {
                if (BCrypt.checkpw(password, rower.getPassword())) {
                    ActiveUsers.addActiveUser(rower.getId());
                    return rower;
                }
                break;
            }
        }
        throw new CredentialNotFoundException("Email or password are wrong.");
    }

    /**
     * Logout the user
     */
    public void logout() {
        ActiveUsers.removeActiveUser(this.getUser().getId());
        this.setUser(null);
    }

    @Override
    public String exportRecords(String entityType) {
        ImportExport exporter = new ImportExport();
        return exporter.exportProcess(entityType);
    }

    @Override
    public File exportRecordsToFile(String entityType, String filepath) {
        ImportExport exporter = new ImportExport();
        return exporter.exportProcessToFile(entityType, filepath);
    }

    @Override
    public List<String> importRecords(String entityType, String fileContents, boolean override) {
        ImportExport importer = new ImportExport();
        return importer.importProcess(entityType, fileContents, override);
    }

    public XMLDatabase getXML() {
        return this.xmlDatabase;
    }

    private void createAdmin() {
        Rower rower = new Rower(
                "administrator",
                Rower.Level.ADVANCED,
                18,
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(Rower.yearsUntilExpired),
                "",
                "admin@boatsclub.com",
                BCrypt.hashpw("boatsclub", BCrypt.gensalt()),
                false,
                true,
                ""
        );
        addRecord("rowers", rower);
    }
}
