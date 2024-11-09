import com.google.gson.Gson;
import model.AuthRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ServerFacade {

    private final String baseUrl = "http://localhost:8080";

    private InputStreamReader makeRequest(HttpURLConnection http, Object body) {
        try {
            if (body != null) {
                var outputStream = http.getOutputStream();
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            http.connect();
            InputStream resBody = http.getInputStream();
            return new InputStreamReader(resBody);

        } catch (Exception e) {throw new RuntimeException("Invalid Connection");
        }
    }

    public AuthRecord loginUser(String username, String password) {
        try {
        URI uri = new URI(this.baseUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        Map<String, String> body = Map.of("username", username, "password", password);

        InputStreamReader inputStreamReader = this.makeRequest(http, body);
        return new Gson().fromJson(inputStreamReader, AuthRecord.class);

         } catch (Exception e) {
            throw new RuntimeException("Invalid Request");
        }
    }

}
