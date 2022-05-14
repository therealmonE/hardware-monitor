package io.github.therealmone.hardware.monitor.bluetooth;

import javax.microedition.io.StreamConnection;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class ConnectionWrapper {
    private final BufferedOutputStream output;

    public ConnectionWrapper(StreamConnection connection) throws IOException {
        this.output = new BufferedOutputStream(connection.openDataOutputStream());
    }

    public void send(byte[] data) throws IOException {
        output.write(data);
        output.flush();
    }

}
