import helpers.ResponseObject;
import model.AuthRecord;
import ui.EscapeSequences;

import java.util.Arrays;

public class Client {

    private String authToken;
    private String username;
    private ServerFacade serverFacade;

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
            default -> this.help();
        };
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

    public String help() {

        String notLoggedin = String.format("""
                %s register <USERNAME> <PASSWORD> <EMAIL> %s - to create an account
                %s login <USERNAME> <PASSWORD> %s - to play chess
                %s quit %s - playing chess
                %s help %s - with possible commands
                """, EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR
        );

        String loggedIn = String.format("""
                %s create <NAME> %s - a game
                %s list %s - games
                %s join <ID> [WHITE|BLACK] %s - a game
                %s observe <ID> %s - a game
                %s logout %s - when you are done
                %s quit %s - playing chess
                %s help %s - with possible commands
                """, EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR
        );

        return this.isLoggedIn() ? loggedIn : notLoggedin;
    }

    public boolean isLoggedIn() {
        return !this.username.isEmpty();
    }

    private String setLogin(ResponseObject res) {
        AuthRecord auth = (AuthRecord) res.data();
        this.authToken = auth.authToken();
        this.username = auth.username();
        return "Logged in as " + this.username;
    }
}
