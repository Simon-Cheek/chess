import com.google.gson.Gson;
import model.AuthRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {

    private final String baseUrl = "http://localhost:8080";

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

            try (InputStream resBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(resBody);
                return new ResponseObject(http.getResponseCode(), new Gson().fromJson(inputStreamReader, returnType));
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid Connection");
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
