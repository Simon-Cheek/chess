import chess.ChessGame;
import helpers.GameIdRecord;
import helpers.GameListRecord;
import helpers.ResponseObject;
import model.GameRecord;

import java.util.ArrayList;

public class AuthClient {

    public static void joinGame(ServerFacade facade, String authToken, ChessGame.TeamColor color, int gameId) {
        if (authToken.isEmpty()) { throw new RuntimeException("Unauthorized"); }
        ResponseObject res = facade.joinGame(authToken, color, gameId);
        if (res.statusCode() == 200) { return; }
        switch (res.statusCode()) {
            case 401 -> throw new RuntimeException("Unauthorized");
            case 403 -> throw new RuntimeException("Already Taken");
            default -> throw new RuntimeException("Server Error");
        }
    }

    public static ArrayList<GameRecord> listGames(ServerFacade facade, String authToken) {
        if (authToken.isEmpty()) { throw new RuntimeException("Unauthorized"); }
        ResponseObject res = facade.listGames(authToken);
        if (res.statusCode() == 200) {
            GameListRecord gameData = (GameListRecord) res.data();
            return gameData.games();
        }
        switch (res.statusCode()) {
            case 401 -> throw new RuntimeException("Unauthorized");
            default -> throw new RuntimeException("Server Error");
        }
    }


    public static int createGame(ServerFacade facade, String authToken, String gameName) {
        if (authToken.isEmpty()) { throw new RuntimeException("Unauthorized"); }
        ResponseObject res = facade.createGame(authToken, gameName);

        if (res.statusCode() == 200) {
            GameIdRecord gameIdRecord = (GameIdRecord) res.data();
            return gameIdRecord.gameId();
        }
        switch (res.statusCode()) {
            case 400 -> throw new RuntimeException("Bad Request");
            case 401 -> throw new RuntimeException("Unauthorized");
            default -> throw new RuntimeException("Server Error");
        }
    }

    public static String logout(ServerFacade facade, String authToken){
        if (authToken.isEmpty()) { throw new RuntimeException("Unauthorized"); }
        ResponseObject res = facade.logoutUser(authToken);
        if (res.statusCode() == 200) { return "Successfully logged out"; }
        switch (res.statusCode()) {
            case 401 -> throw new RuntimeException("Unauthorized");
            default -> throw new RuntimeException("Server Error");
        }
    }

}
