package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.process.impl.ControlPayloadProcess;
import org.junit.Test;

public class DcomServiceFutureTest {

    @Test
    public void connect() {
        ClientFuture.connect("127.0.0.1", 8080);
    }

    @Test
    public void send() {
        ClientFuture.send("Hello World!", new ControlPayloadProcess());
    }

    @Test
    public void close() {
        ClientFuture.close();
    }
}