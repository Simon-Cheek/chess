package dataaccess;

import model.AuthRecord;
import model.GameRecord;
import model.UserRecord;

import java.util.ArrayList;

public class DataAccess {

    // Mock data to use without DB
    private ArrayList<UserRecord> userRecords;
    private ArrayList<GameRecord> gameRecords;
    private ArrayList<AuthRecord> authRecords;



    public DataAccess() {
        this.userRecords = new ArrayList<UserRecord>();
        this.gameRecords = new ArrayList<GameRecord>();
        this.authRecords = new ArrayList<AuthRecord>();
    }
}
