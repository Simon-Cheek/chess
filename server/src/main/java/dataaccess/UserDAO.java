package dataaccess;

import model.UserRecord;
import java.util.ArrayList;

public class UserDAO {

    // Mock data to use without DB
    private ArrayList<UserRecord> userRecords;

    public UserDAO() {
        this.userRecords = new ArrayList<UserRecord>();
    }

    // Returns UserRecord object that matches given username
    public UserRecord findUser(String username) {
        for (UserRecord user : this.userRecords) {
            if (user.username().equals(username)) return user;
        }
        return null;
    }

    // Creates a new user
    public void createUser(UserRecord user) {
        this.userRecords.add(user);
    }

    public void deleteUsers() {
        this.userRecords = new ArrayList<UserRecord>();
    }

}