package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameRecord;
import model.UserRecord;

import java.sql.Connection;
import java.util.ArrayList;

public class DBGameDAO {

    public DBGameDAO() {
        configureGameDB();
    }

    public void deleteGames() throws ResponseException {
        String statement = "TRUNCATE gameData";
        ExecuteUpdate.executeUpdate(statement);
    }

    public int createGame(String name) throws ResponseException {
        String statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?)";
        ChessGame newGame = new ChessGame();
        return ExecuteUpdate.executeUpdate(statement, null, null, name, new Gson().toJson(newGame));
    }

    public void joinGame(GameRecord game, ChessGame.TeamColor color, String username) throws ResponseException {
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            int gameId = game.gameID();
            String statement = "UPDATE gameData SET whiteUsername = ? WHERE id = ?";
            ExecuteUpdate.executeUpdate(statement, username, gameId);
        } else {
            int gameId = game.gameID();
            String statement = "UPDATE gameData SET blackUsername = ? WHERE id = ?";
            ExecuteUpdate.executeUpdate(statement, username, gameId);
        }
    }

    public GameRecord findGame(String name) throws ResponseException {
        return ExecuteUpdate.executeGameQuery("SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameName = ?", name);
    }

    public GameRecord findGame(int gameId) throws ResponseException {
        return ExecuteUpdate.executeGameQuery("SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData WHERE id = ?", gameId);
    }

    public void persistGame(GameRecord game) throws ResponseException {
        int gameId = game.gameID();
        String statement = "UPDATE gameData SET game = ? WHERE id = ?";
        ExecuteUpdate.executeUpdate(statement, new Gson().toJson(game.game()), gameId);
    }


    public ArrayList<GameRecord> getAllGames() throws ResponseException {
        ArrayList<GameRecord> allGames = new ArrayList<GameRecord>();
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * from gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int gameId = rs.getInt("id");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String name = rs.getString("gameName");
                        String gameString = rs.getString("game");
                        ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                        allGames.add(new GameRecord(gameId, whiteUsername, blackUsername, name, game));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return allGames;
    }

    private final String setup = """
            CREATE TABLE IF NOT EXISTS gameData (
            `id` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` TEXT NOT NULL,
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;

    private void configureGameDB() {
        ExecuteUpdate.configureDB(setup);
    }
}