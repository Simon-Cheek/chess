import helpers.GameIdRecord;
import helpers.ResponseObject;

public class AuthClient {


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
