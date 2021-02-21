package interfaces;

import entities.Rower;
import exceptions.AlreadyLoggedInException;

import javax.security.auth.login.CredentialNotFoundException;
import java.io.File;
import java.util.List;

public interface Engine {
    Rower authenticate(String email, String password) throws CredentialNotFoundException, AlreadyLoggedInException;

    void logout();

    String exportRecords(String entityType);

    File exportRecordsToFile(String entityType, String filepath);

    List<String> importRecords(String entityType, String fileContents, boolean override);
}
