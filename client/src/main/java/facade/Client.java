package facade;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import helpers.MoveGenerator;
import ui.BoardBuilder;
import helpers.HelpInfo;
import helpers.ResponseObject;
import model.AuthRecord;
import model.GameRecord;
import ui.EscapeSequences;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private String authToken;
    private String username;
    private final ServerFacade serverFacade;
    private final WebSocketFacade webSocketFacade;
    private ArrayList<GameRecord> games;
    private GameRecord currentGame;

    public Client(NotificationHandler handler) {
        this.username = "";
        this.authToken = "";
        this.serverFacade = new ServerFacade();
        this.webSocketFacade = new WebSocketFacade("http://localhost:8080", handler);
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
            case "redraw" -> this.redrawGame();
            case "leave" -> this.leaveGame();
            case "make" -> this.makeMove(params);
            case "resign" -> this.resignGame();
            case "highlight" -> this.highlightBoard(params);
            default -> HelpInfo.help(this.isLoggedIn(), this.isInGame());
        };
    }
    public String postGameStatus() {

        String whitePlayer = this.currentGame.whiteUsername();
        String blackPlayer = this.currentGame.blackUsername();

        String result = "\n";
        if (this.currentGame.game().isFinished()) {
            ChessGame.TeamColor color = this.currentGame.game().getWinningColor();
            if (color.equals(ChessGame.TeamColor.WHITE)) {
                result += String.format("Checkmate, %s (white) wins!\n", whitePlayer);
            } else if (color.equals(ChessGame.TeamColor.BLACK)) {
                result += String.format("Checkmate, %s (black) wins!\n", blackPlayer);
            } else {
                result += ("Stalemate! No one wins :(\n");
            }
        } else {
            if (this.currentGame.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                result += String.format("%s (white) is in Check!\n", whitePlayer);
            }
            if (this.currentGame.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                result += String.format("%s (black) is in Check!\n", blackPlayer);
            }
            if (this.currentGame.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                result += ("It is White's turn!\n");
            } else {
                result += ("It is Black's turn!\n");
        }}
        return result;
    }

    public void refreshGame(GameRecord game) {
        this.currentGame = game;
        System.out.println(this.redrawGame());
    }

    public String highlightBoard(String[] params) {

        if (params.length == 0) { throw new RuntimeException("Must supply a position"); }
        ChessPosition pos = MoveGenerator.getPosition(params[0]);

        if (this.currentGame.blackUsername() != null && this.currentGame.blackUsername().equals(this.username)) {
            return BoardBuilder.buildBlackBoard(this.currentGame.game(), pos);
        } else {
            return BoardBuilder.buildWhiteBoard(this.currentGame.game(), pos);
        }
    }

    public String resignGame() {
        if (this.username.isEmpty()) { throw new RuntimeException("Not logged in"); }
        if (this.currentGame == null) { throw new RuntimeException("Not in a game"); }

        System.out.print("Type 'yes' to confirm resign: ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line.equals("yes")) {
            this.webSocketFacade.resignGame(this.authToken, this.currentGame.gameID());
            return "Resigning...";
        } else {
            return "Not resigning!";
        }
    }

    public String makeMove(String[] params) {
        if (this.username.isEmpty()) { throw new RuntimeException("Not logged in"); }
        if (this.currentGame == null) { throw new RuntimeException("Not in a game"); }
        if (this.currentGame.game().isFinished()) { throw new RuntimeException("Game is finished"); }
        if (params.length != 4 && params.length != 3) { throw new RuntimeException("Invalid Argument Length"); }
        if (!params[0].equals("move")) { throw new RuntimeException("Invalid command."); }
        ChessPiece.PieceType piece;
        if (params.length == 4) {
            params[3] = params[3].toLowerCase();
            piece = switch (params[3]) {
                case "queen" -> ChessPiece.PieceType.QUEEN;
                case "bishop" -> ChessPiece.PieceType.BISHOP;
                case "rook" -> ChessPiece.PieceType.ROOK;
                case "knight" -> ChessPiece.PieceType.KNIGHT;
                default -> null;
            };
        } else {
            piece = null;
        }
        ChessMove move = MoveGenerator.getMove(params[1], params[2], piece);
        this.webSocketFacade.makeMove(this.authToken, this.currentGame.gameID(), move);
        return "Making move...";
    }

    public String leaveGame() {
        if (this.username.isEmpty()) { throw new RuntimeException("Not logged in"); }
        if (this.currentGame == null) { throw new RuntimeException("Not in a game"); }
        this.webSocketFacade.leaveGame(this.authToken, this.currentGame.gameID());
        this.currentGame = null;
        return "Leaving the game...";
    }

    public String redrawGame() {
        if (this.username.isEmpty()) { throw new RuntimeException("Not logged in"); }
        if (this.currentGame == null) { throw new RuntimeException("Not in a game"); }
        if (this.currentGame.blackUsername() != null && this.currentGame.blackUsername().equals(this.username)) {
            return BoardBuilder.buildBlackBoard(this.currentGame.game(), null) + this.postGameStatus();
        } else {
            return BoardBuilder.buildWhiteBoard(this.currentGame.game(), null) + this.postGameStatus();
        }
    }

    public String observeGame(String[] params) {
        if (params.length != 1) { throw new RuntimeException("Invalid Argument Length"); }
        ClientGameInfo gameInfo = this.getGameInfo(params[0]);
        String statement = String.format("Observing Game %d) %s\n", gameInfo.gameNumber(), gameInfo.gameName());
        GameRecord game = this.games.get(gameInfo.gameNumber() - 1);
        this.webSocketFacade.connectToGame(this.authToken, game.gameID());
        return statement;
    }


    public String joinGame(String[] params) {
        if (params.length != 2) { throw new RuntimeException("Invalid Argument Length"); }

        if (this.currentGame != null) { throw new RuntimeException("Please leave your current game!"); }

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
        GameRecord gameRecord = this.games.get(gameInfo.gameNumber() - 1);
        this.currentGame = gameRecord;
        this.webSocketFacade.connectToGame(this.authToken, gameRecord.gameID());

        return statement + "\n" + HelpInfo.help(this.isLoggedIn(), this.isInGame());
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
        this.currentGame = null;
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
                return this.setLogin(res) + "\n" + HelpInfo.help(this.isLoggedIn(), this.isInGame());
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

    private boolean isInGame() {
        return this.currentGame != null;
    }

    private String setLogin(ResponseObject res) {
        AuthRecord auth = (AuthRecord) res.data();
        this.authToken = auth.authToken();
        this.username = auth.username();
        return "Logged in as " + this.username;
    }
}
