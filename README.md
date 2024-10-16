# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9FMr73K2Xwfks4ygTAALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhyMDcryBqCsKMDAaGlFumyuHlAajrOgSBEsuUVDAMgWiZCRBrvvorxLBRTLsZGNEAOIUjAsbBtopi4Wy-5puhPLZrmmA6RxV5poBAxkaMkHVlOQazs2E6TN+plstkPYwP2TilkBlkgTZfTTvZ86OdBmkuacbkYOUAAsTgAIzeX0ozqMA5LWZSwRBtASAAF4oMsoU-q555gOUACsA6JclqipX5RWUN4UBOWYnCrt4fiBF4KDoHuB6+Mwx7pJkmCRcw8ElOUFTSAAoru031NNzQtA+qhPt0gWNug7a-mcQIAbZfrZTl8SJNZrYwXtO2cTAyH2P1aF9b6mEYjhcp4XxbECTA5JUjSG1zrJZoRoUlq0TytoGkKYTTg6rrSuN4J2rQWn4Z9pI3Y9v2ULCBqA1RCnlNIKDcJkqlBvGb3abBJbof1BkIHm1NXaZVxTNVtVnc1RURSVfaVT0BzbcVOSlTAsUJeZ7NpVWGXTkd+Vc+FhSjeV-NsyyHNVvVUCNc1y5teugSQrau7QjAimjqyg2niNJUmdQ16KfNS32KO612ZtaBC8mTOswFWVQLlJ3BVBF2pvbTryjd0IW8lAYewDPGJh9clfT9JH-egePySDNF0RDHRQzAMMaXD5oI1H3EoynQNEWACjKrHaiwtn8O5+UynME3GjV7tqYd5bRlMxH+19K7yWQd0FTmePKAAJLSJBcW9gAzNFTwnmJvm7DM3RTDoCCgA2BoVp8TyzwAcqO-wwI0zkOztKseWrY+W5BFTNPfxw8yLMXxVVGtpZTFlgHXKCtCpKy7L-GAFUvKS0AXVFy00dbQD1q1Tw7UNzYB8FAbA3B4C6lJk3FIQ0zwixHmmG8DQXZu0ynWT2Q5L6ji-h2H2l0-Zy0DsdAgp1HLn1HFfKyjkw5wUvAhcono9RN1hJIzITdnrYSTu9GQ-F0bpz+gnLOrFU7l3bmDeihcmIl3kNooGEdEZV0pqjHR7oYAADNwbSKYaMVuuj2TlHzubUcRcm6CLEGXCMFdEIwEVMqJRGpVG2NkSgJxAjyKmPxno6MXjRgUwQlTdhKSUBwEISgemjNLoUL9rPOeHxQ7e2VrzZ+cCpglLKasCBD9hbuVijUpKCDrIACFgC2gUWARWTSf4tP-vAlKQC+ggMOlw8BLDH5VNgQAsZiCmnIN1qFfWGDDYBEsMTZCyQYAACkIA8iyYEA+R9bbkPGteKolI7wtFnu7ehc4hx4OADsqAcAIDISgLMEp0hZkZP7olN5Hyvk-JWAAdRYHPBaLROm7gUHAAA0rvJIr9RgLyXqvdeB1UhHWDpzUKxkgnlAAFbHLQNIilPJ5EoDRC9cJ1ja7fWIho55WiAnUQ8eDJGsAjHk1LpE8xlcOhMpruGcoDjOBN06ZYBesJ-muOBu4-RtpiFMQXgknORRxFZPFSotGtiaVUtHLCUFlBwXQFmE3ZV3LDmUtOckDIqR4DfOgAanS5Qjm0tHPkoehTrlmXVksolgKhlRWqaWcNlToGtMWTVcZ3Ten0qwv0xp39Y3DIliGxNflJn4umQVGNUDhkrwTZrKYDddQcDCPUYAc4BmZtLZGhZoy83WSKqs1B6z0Frg6gELw7yuxelgMAbAeDCDBxITbUaRTKgzTmgtJaxgKl92BFcERrCxGR2CSAbgeBpCdJbgagJ5QODEwpCgBuCAT1coJjIS9pMb2et9tGgN4cg2brXU-Ty77uZZtbS-KWyzjjdqar2lcQA

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
