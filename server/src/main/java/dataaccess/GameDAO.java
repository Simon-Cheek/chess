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
        counter++;
        return id;
    }

    public GameRecord findGame(String name) {
        for (GameRecord game : gameRecords) {
            if (game.gameName().equals(name)) return game;
        }
        return null;
    }

    public ArrayList<GameRecord> getAllGames() {
        return this.gameRecords;
    }
}