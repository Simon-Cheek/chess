# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[Sequence Diagram](https://sequencediagram.org/index.html#initialData=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)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
