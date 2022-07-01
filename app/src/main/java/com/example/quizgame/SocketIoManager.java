package com.example.quizgame;
import java.net.URISyntaxException;
import java.util.function.Function;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIoManager {
    private static Socket socket = null;

    public void connect(String ip) {
        try {
            if (this.socket == null) {
                System.out.println(ip);
                this.socket = IO.socket(ip);
                this.socket.connect();
            } else {
                if (!this.socket.connected()) {
                    this.socket.connect();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new Error();
        }
    }

    public void disconnect() {
        this.socket.disconnect();
    }

    public Socket getSocket() {
        return this.socket;
    }

}
