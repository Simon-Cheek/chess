package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameRecord;

import java.sql.ResultSet;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class ExecuteUpdate {

    public static int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {

                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p); }
                    else if (param instanceof Integer p) {ps.setInt(i + 1, p); }
                    else if (param == null) {ps.setNull(i + 1, NULL);}
                }
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) { return rs.getInt(1); }
            }
        } catch(Exception ex) {
            throw new ResponseException(String.format("Database Update Error: %s", ex.getMessage()), 500);
        }
        return 0;
    }

    public static GameRecord executeGameQuery(String query, Object param) throws ResponseException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            if (param instanceof String p) { ps.setString(1, p); }
            else if (param instanceof Integer p) { ps.setInt(1, p); }

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int gameId = rs.getInt("id");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameString = rs.getString("game");
                    ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                    return new GameRecord(gameId, whiteUsername, blackUsername, rs.getString("gameName"), game);
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }
}
