package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameRecord;

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

    public GameRecord findGame(String name) {
        for (GameRecord game : gameRecords) {
            if (game.gameName().equals(name)) { return game; }
        }
        return null;
    }

    public GameRecord findGame(int gameID) {
        for (GameRecord game : gameRecords) {
            if (game.gameID() == gameID) { return game; }
        }
        return null;
    }

    public ArrayList<GameRecord> getAllGames() {
        return this.gameRecords;
    }

    private final String setup = """
            CREATE TABLE IF NOT EXISTS gameData (
            `id` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` TEXT NOT NULL
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;

    private void configureGameDB() {
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