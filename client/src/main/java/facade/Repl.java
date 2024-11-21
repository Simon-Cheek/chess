package facade;

import ui.EscapeSequences;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {

    private Client client;

    public Repl() {
        this.client = new Client();
    }

    public void run() {
        System.out.println("Welcome to Chess!");

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

    public void notify(ServerMessage message) {}

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
