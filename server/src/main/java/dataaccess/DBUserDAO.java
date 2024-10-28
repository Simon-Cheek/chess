package dataaccess;

import exception.ResponseException;
import model.UserRecord;

import java.sql.Connection;
import java.util.ArrayList;

public class DBUserDAO {

    public DBUserDAO() {
        configureUserDB();
    }

    // Returns UserRecord object that matches given username
    public UserRecord findUser(String username) {
        for (UserRecord user : this.userRecords) {
            if (user.username().equals(username)) { return user; }
        }
        return null;
    }

    // Creates a new user
    public void createUser(UserRecord user) throws ResponseException {
        String statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        ExecuteUpdate.executeUpdate(statement, user.username(), user.password(),user.email());
    }

    public void deleteUsers() throws ResponseException {
        String statement = "TRUNCATE userData";
        ExecuteUpdate.executeUpdate(statement);

    }

    private final String setup = """
            CREATE TABLE IF NOT EXISTS userData (
            `id` int NOT NULL AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `password` TEXT NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;

    private void configureUserDB() {
        try {
            DatabaseManager.createDatabase();
            try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement(setup)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch(Exception ex) {
            throw new RuntimeException();
        }
    }

}