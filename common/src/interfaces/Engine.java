package interfaces;

import entities.Rower;
import exceptions.AlreadyLoggedInException;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.List;

public interface Engine {
    Rower authenticate(String email, String password) throws CredentialNotFoundException, AlreadyLoggedInException;

    void logout();

    String exportRecords(String entityType);

    List<String> importRecords(String entityType, String fileContents, boolean override);
}
