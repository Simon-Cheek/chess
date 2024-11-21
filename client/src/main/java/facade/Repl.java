package facade;

import helpers.HelpInfo;
import ui.EscapeSequences;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {

    private Client client;

    public Repl() {
        this.client = new Client(this);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.println(HelpInfo.help(false, false));

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            this.printPrompt();
            String line = scanner.nextLine();
            try {
                result = this.client.parse(line);
                System.out.print(result);
            } catch (Exception e) {
                this.printError(e);
            }
            System.out.println();
        }
    }

    public String wrapInYellow(String s) {
        return EscapeSequences.SET_TEXT_COLOR_YELLOW + s + EscapeSequences.RESET_TEXT_COLOR;
    }

    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION:
                System.out.println(this.wrapInYellow(message.getMessage()));
                break;
            case ERROR:
                System.out.println(this.wrapInYellow(message.getErrorMessage()));
                break;
            case LOAD_GAME:
                this.client.refreshGame(message.getGame());
                break;
            default:
                System.out.println("Unexpected msg from WS");
        }
        this.printPrompt();
    }

    private void printPrompt() {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
        System.out.print(client.isLoggedIn() ? "[LOGGED IN]" : "[LOGGED OUT]");
        System.out.print(" >>> ");
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(" ");
    }

    private void printError(Exception e) {
        System.out.println(EscapeSequences.SET_BG_COLOR_RED + "Error: " + e.getMessage() + EscapeSequences.RESET_BG_COLOR);
    }

}
