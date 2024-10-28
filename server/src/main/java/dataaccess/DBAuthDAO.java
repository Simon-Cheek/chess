package dataaccess;

import exception.ResponseException;
import model.AuthRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class DBAuthDAO {

    public DBAuthDAO() throws ResponseException, DataAccessException {
        configureAuthDB();
    }

    // Creates Auth for a user and adds to the DB
    public AuthRecord createAuth(String username) throws DataAccessException, ResponseException {
        String token = UUID.randomUUID().toString();
        AuthRecord auth = new AuthRecord(token, username);
        String statement = "INSERT INTO authData (user, authToken) VALUES (?, ?)";
        executeUpdate(statement, auth.username(), auth.authToken());
        return auth;
    }

    private void executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {

                    // Only working with Strings in AuthDAO
                    var param = params[i];
                    ps.setString(i + 1, (String)param);
                }
                ps.executeUpdate();
            }
        } catch(Exception ex) {
            throw new ResponseException(String.format("Database Update Error: %s", ex.getMessage()), 500);
        }
    }

        public AuthRecord getAuthByToken(String authToken) throws ResponseException {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT user, authToken FROM authData WHERE authToken=?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, authToken);
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String user = rs.getString("user");
                            String token = rs.getString("authToken");
                            return new AuthRecord(token, user);
                        }
                    }
                }
            } catch (Exception e) {
                throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
            }
        return null;
    }

    public void deleteAuthByToken(String authToken) throws ResponseException {
        String statement = "DELETE FROM authData WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    // Clear the Database
    public void deleteAuths() throws ResponseException {
        String statement = "TRUNCATE authData";
        executeUpdate(statement);
    }

    private final String setup = """
            CREATE TABLE IF NOT EXISTS authData (
            `id` int NOT NULL AUTO_INCREMENT,
            `user` varchar(256) NOT NULL,
            `authToken` TEXT DEFAULT NULL,
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;

    private void configureAuthDB() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement(setup)) {
                    preparedStatement.executeUpdate();
                }
        } catch(SQLException ex) {
            throw new ResponseException(String.format("Database Config Error: %s", ex.getMessage()), 500);
        }
    }
}
