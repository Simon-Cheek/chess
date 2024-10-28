package dataaccess;

import exception.ResponseException;

public class ExecuteUpdate {

    public static void executeUpdate(String statement, Object... params) throws ResponseException {
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
}
