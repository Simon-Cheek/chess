package dataaccess;

import chess.ChessGame;
import model.GameRecord;

import java.util.ArrayList;

public class GameDAO {

    // Mock data to use without DB
    private ArrayList<GameRecord> gameRecords;
    private int counter;

    public GameDAO() {
        this.gameRecords = new ArrayList<GameRecord>();
        this.counter = 1;
    }

    public void deleteGames() {
        this.gameRecords = new ArrayList<GameRecord>();
        this.counter = 1;
    }

    public int createGame(String name) {
        int id = counter;
        GameRecord game = new GameRecord(id, null, null, name, new ChessGame());
        this.gameRecords.add(game);
        counter++;
        return id;
    }

    public void joinGame(GameRecord game, ChessGame.TeamColor color, String username) {
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            GameRecord newGame = new GameRecord(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            this.gameRecords.remove(game);
            this.gameRecords.add(newGame);
        } else {
            GameRecord newGame = new GameRecord(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            this.gameRecords.remove(game);
            this.gameRecords.add(newGame);
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
}