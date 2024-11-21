package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String userName;
    public Session session;
    public int gameId;

    public Connection(String userName, Session session) {
        this.userName = userName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        System.out.println("About to send:");
        System.out.println(msg);
        session.getRemote().sendString(msg);
    }
}
