import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    private String authToken;
    private boolean loggedIn;

    private ServerFacade serverFacade;

    public Client() {
        this.loggedIn = false;
        this.authToken = "";
        this.serverFacade = new ServerFacade();
    }

    public String parse(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = tokens.length > 0 ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> "register";
            case "login" -> "login";
            case "quit" -> "quit";
            default -> this.help();
        };
    }

    public String help() {
        return "Help";
    }

    public boolean getLoggedIn() {
        return this.loggedIn;
    }

    public void evalQuery() {}


}
