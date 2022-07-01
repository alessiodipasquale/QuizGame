package com.example.quizgame;
import java.net.URISyntaxException;
import java.util.function.Function;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIoManager {
    private static Socket socket = null;

    public void connect() {
        try {
            if (this.socket == null) {
                this.socket = IO.socket("http://192.168.1.57:3000");
                this.socket.connect();
            }
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

}
