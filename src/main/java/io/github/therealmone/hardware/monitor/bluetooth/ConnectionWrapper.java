package io.github.therealmone.hardware.monitor.bluetooth;

import javax.microedition.io.StreamConnection;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class ConnectionWrapper {

    private final BufferedInputStream input;
    private final BufferedOutputStream output;

    public ConnectionWrapper(StreamConnection connection) {
        try {
            this.input = new BufferedInputStream(connection.openDataInputStream());
            this.output = new BufferedOutputStream(connection.openDataOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(byte[] data) {
        try {
            output.write(data);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] receive() {
        try {
            byte[] buffer = new byte[1000];
            input.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
