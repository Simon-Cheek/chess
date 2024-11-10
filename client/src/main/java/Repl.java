import java.util.Scanner;

public class Repl {

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
                System.out.print("Result:" + result);
            } catch (Exception e) {
                this.printError();
            }
            System.out.println();
        }
    }

    private void printPrompt() {
        System.out.print(client.getLoggedIn() ? "[LOGGED IN]" : "[LOGGED OUT]");
        System.out.print(" >>> ");
    }

    private void printError() {
        System.out.println("Error: Invalid Prompt");
    }

}
