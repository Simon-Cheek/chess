import chess.ChessGame;
import helpers.BoardBuilder;
import helpers.HelpInfo;
import helpers.ResponseObject;
import model.AuthRecord;
import model.GameRecord;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    private String authToken;
    private String username;
    private final ServerFacade serverFacade;

    private ArrayList<GameRecord> games;

    public Client() {
        this.username = "";
        this.authToken = "";
        this.serverFacade = new ServerFacade();
    }

    public String parse(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = tokens.length > 0 ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> this.register(params);
            case "login" -> this.login(params);
            case "quit" -> "quit";
            case "logout" -> this.logout();
            case "create" -> this.create(params);
            case "list" -> this.listGames();
            case "join" -> this.joinGame(params);
            case "observe" -> this.observeGame(params);
            default -> HelpInfo.help(this.isLoggedIn());
        };
    }

    public String observeGame(String[] params) {
        if (params.length != 1) { throw new RuntimeException("Invalid Argument Length"); }
        ClientGameInfo gameInfo = this.getGameInfo(params[0]);
        String statement = String.format("Observing Game %d) %s\n", gameInfo.gameNumber(), gameInfo.gameName());
        return statement + BoardBuilder.buildBoard(this.games.get(gameInfo.gameNumber() - 1).game());
    }


    public String joinGame(String[] params) {
        if (params.length != 2) { throw new RuntimeException("Invalid Argument Length"); }

        ClientGameInfo gameInfo = this.getGameInfo(params[0]);

        String colorString = params[1].toLowerCase();
        if (!colorString.equals("white") && !colorString.equals("black")) {
            throw new RuntimeException("Invalid Color");
        }
        ChessGame.TeamColor color =
                colorString.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

        AuthClient.joinGame(this.serverFacade, this.authToken, color, gameInfo.gameId());
        String statement =
                String.format("Successfully joined game %d) %s\n", gameInfo.gameNumber(), gameInfo.gameName());
        return statement + BoardBuilder.buildBoard(this.games.get(gameInfo.gameNumber() - 1).game());
    }

    public String listGames() {
        ArrayList<GameRecord> games = AuthClient.listGames(this.serverFacade, this.authToken);
        this.games = games;

        StringBuilder gameInfo = new StringBuilder();
        gameInfo.append(EscapeSequences.SET_TEXT_ITALIC);

        for (int i = 0; i < games.size(); i++) {
            GameRecord game = games.get(i);
            gameInfo.append(String.format(
                    "%d) Name: %s, White Player: %s, Black Player: %s\n",
                    i + 1, game.gameName(), game.whiteUsername(), game.blackUsername()));
        }
        gameInfo.append(EscapeSequences.RESET_TEXT_ITALIC);
        return gameInfo.toString();
    }

    public String create(String[] params) {
        if (params.length != 1) { throw new RuntimeException("Invalid Argument Length"); }
        AuthClient.createGame(this.serverFacade, this.authToken, params[0]);
        return "Created game: " + params[0];
    }

    public String logout() {
        String res = AuthClient.logout(this.serverFacade, this.authToken);
        this.authToken = "";
        this.username = "";
        return res;
    }

    public String login(String[] params) {
        if (params.length != 2) {
            throw new RuntimeException("Invalid Argument Length");
        } else if (this.isLoggedIn()) {
            throw new RuntimeException("Error: Already Logged In");
        } else {
            ResponseObject res = serverFacade.loginUser(params[0], params[1]);
            if (res.statusCode() == 200) {
                return this.setLogin(res);
            }
            switch (res.statusCode()) {
                case 400 -> throw new RuntimeException("Bad Request");
                case 401 -> throw new RuntimeException("Unauthorized");
                default -> throw new RuntimeException("Server Error");
            }
        }
    }

    public String register(String[] params) {
        if (params.length != 3) {
            throw new RuntimeException("Invalid Argument Length");
        } else if (this.isLoggedIn()) {
            throw new RuntimeException("Error: Already Logged In");
        } else {
            ResponseObject res = serverFacade.registerUser(params[0], params[1], params[2]);
            if (res.statusCode() == 200) {
                return this.setLogin(res);
            }
            switch (res.statusCode()) {
                case 400 -> throw new RuntimeException("Bad Request");
                case 403 -> throw new RuntimeException("Username Already Taken");
                default -> throw new RuntimeException("Server Error");
            }
        }
    }


    public boolean isLoggedIn() {
        return !this.username.isEmpty();
    }

    public record ClientGameInfo(int gameNumber, int gameId, String gameName) {}

    private ClientGameInfo getGameInfo(String gameString) {
        int gameNumber;
        int gameId;
        String gameName;
        try {
            gameNumber = Integer.parseInt(gameString);
            gameId = this.games.get(gameNumber - 1).gameID();
            gameName = this.games.get(gameNumber - 1).gameName();
        } catch (Exception e) {
            throw new RuntimeException("Invalid ID or No Games. List Games to view IDs.");
        }
        return new ClientGameInfo(gameNumber, gameId, gameName);
    }

    private String setLogin(ResponseObject res) {
        AuthRecord auth = (AuthRecord) res.data();
        this.authToken = auth.authToken();
        this.username = auth.username();
        return "Logged in as " + this.username;
    }
}
