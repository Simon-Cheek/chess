package dataaccess;

import model.GameRecord;

import java.util.ArrayList;

public class GameDAO {

    // Mock data to use without DB
    private ArrayList<GameRecord> gameRecords;

    public GameDAO() {
        this.gameRecords = new ArrayList<GameRecord>();
    }

    public void deleteGames() {
        this.gameRecords = new ArrayList<GameRecord>();
    }
}