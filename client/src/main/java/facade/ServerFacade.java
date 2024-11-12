package facade;

import chess.ChessGame;
import com.google.gson.Gson;
import helpers.GameIdRecord;
import helpers.GameListRecord;
import helpers.ResponseObject;
import model.AuthRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {

    private String baseUrl;
    private int port;

    public ServerFacade() {
        this.port = 8080;
        this.baseUrl = "http://localhost:" + String.valueOf(this.port);
    }

    public ServerFacade(int port) {
        this.port = port;
        this.baseUrl = "http://localhost:" + String.valueOf(this.port);
    }

    private <T> ResponseObject makeRequest(HttpURLConnection http, Object body, Class<T> returnType) {
        try {
            if (body != null) {
               try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(body);
                    outputStream.write(jsonBody.getBytes());
               }
            }
            http.connect();
            if (http.getResponseCode() >= 400) {
                return new ResponseObject(http.getResponseCode(), null);
            }
            if (returnType != null) {
                try (InputStream resBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(resBody);
                    return new ResponseObject(http.getResponseCode(), new Gson().fromJson(inputStreamReader, returnType));
                }
            } else { return new ResponseObject(http.getResponseCode(), null); }
        } catch (Exception e) {
            throw new RuntimeException("Invalid Connection");
        }
    }

    public ResponseObject joinGame(String authToken, ChessGame.TeamColor color, int gameId) {
        try {
            URI uri = new URI(this.baseUrl + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("PUT");
            http.setRequestProperty("Authorization", authToken);
            http.setDoOutput(true);

            Map<String, String> body = Map.of("playerColor", color.toString(), "gameID", String.valueOf(gameId));

            return this.makeRequest(http, body, null);

        } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }
    }

    public ResponseObject listGames(String authToken) {
        try {
            URI uri = new URI(this.baseUrl + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Authorization", authToken);
            return this.makeRequest(http, null, GameListRecord.class);

        } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }
    }

    public ResponseObject createGame(String authToken, String gameName) {
        try {
            URI uri = new URI(this.baseUrl + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", authToken);
            Map<String, String> body = Map.of("gameName", gameName);

            return this.makeRequest(http, body, GameIdRecord.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }
    }

    public ResponseObject logoutUser(String authToken) {
        try {
            URI uri = new URI(this.baseUrl + "/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("DELETE");
            http.setRequestProperty("Authorization", authToken);
            return this.makeRequest(http, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }

    }

    public ResponseObject registerUser(String username, String password, String email) {
        try {
            URI uri = new URI(this.baseUrl + "/user");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            Map<String, String> body = Map.of("username", username, "password", password, "email", email);

            return this.makeRequest(http, body, AuthRecord.class);

        } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }
    }

    public ResponseObject loginUser(String username, String password) {
        try {
        URI uri = new URI(this.baseUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        Map<String, String> body = Map.of("username", username, "password", password);

        return this.makeRequest(http, body, AuthRecord.class);

         } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }
    }
}
