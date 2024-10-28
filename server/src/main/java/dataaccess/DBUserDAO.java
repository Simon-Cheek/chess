package dataaccess;

import exception.ResponseException;
import model.UserRecord;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;

public class DBUserDAO {

    public DBUserDAO() {
        configureUserDB();
    }

    // Returns UserRecord object that matches given username
    public UserRecord findUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        String pass = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserRecord(user, pass, email);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    // Creates a new user with HASHED password
    public void createUser(UserRecord user) throws ResponseException {
        String statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        ExecuteUpdate.executeUpdate(statement, user.username(), hashedPassword,user.email());
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