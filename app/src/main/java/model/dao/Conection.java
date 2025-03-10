package model.dao;

import java.io.IOException;
import java.net.Socket;

public class Conection {

    private Socket socket;

    public Socket connect() {
        try {
            socket = new Socket("10.5.104.43", 23456);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
