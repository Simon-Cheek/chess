package dataaccess;

import model.AuthRecord;

import java.util.ArrayList;
import java.util.UUID;

public class AuthDAO {

    // Mock data to use without DB
    private ArrayList<AuthRecord> authRecords;

    public AuthDAO() {
        this.authRecords = new ArrayList<AuthRecord>();
    }

    // Creates Auth for a user and adds to the Record List
    public AuthRecord createAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthRecord auth = new AuthRecord(token, username);
        this.authRecords.add(auth);
        return auth;
    }

    public AuthRecord getAuthByToken(String authToken) {
        for (AuthRecord auth : authRecords) {
            if (authToken.equals(auth.authToken())) return auth;
        }
        return null;
    }

    public void deleteAuthByToken(String authToken) {
        this.authRecords.removeIf(auth -> auth.authToken().equals(authToken));
    }

    // Clear the Database
    public void deleteAuths() {
        this.authRecords = new ArrayList<AuthRecord>();
    }

}
