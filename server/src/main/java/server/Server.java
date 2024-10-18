package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthRecord;
import model.UserRecord;
import spark.*;

import service.Service;

public class Server {

    private Service service;

    public Server() {
        this.service = new Service();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.delete("/db", this::deleteAll);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        UserRecord user = new Gson().fromJson(req.body(), UserRecord.class);
        AuthRecord auth = this.service.registerUser(user);
        return new Gson().toJson(auth);
    }

    private Object deleteAll(Request req, Response res) {
        res.status(200);
        return "{}";
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(new Gson().toJson(ex));
    }
}
