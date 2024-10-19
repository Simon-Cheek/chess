package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthRecord;
import model.LoginRequest;
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
        Spark.post("/session", this::loginUser);
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

    private Object loginUser(Request req, Response res) throws ResponseException {
        System.out.println("Starting the login process");
        LoginRequest login = new Gson().fromJson(req.body(), LoginRequest.class);
        AuthRecord auth = this.service.loginUser(login);
        System.out.println("This should not be reached");
        return new Gson().toJson(auth);
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        UserRecord user = new Gson().fromJson(req.body(), UserRecord.class);
        AuthRecord auth = this.service.registerUser(user);
        return new Gson().toJson(auth);
    }

    private Object deleteAll(Request req, Response res) {
        res.status(200);
        this.service.deleteDB();
        return "{}";
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());

        record ErrorMessage(String message) {}

        ErrorMessage message = new ErrorMessage(ex.getMessage());
        res.body(new Gson().toJson(message));
    }
}
