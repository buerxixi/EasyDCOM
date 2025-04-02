package om.github.buerxixi.easydcom.service;

import org.junit.Test;

public class DcomServiceFutureTest {

    @Test
    public void connect() {
        DcomServiceFuture.connect("127.0.0.1", 8080);
    }

    @Test
    public void send() {
        DcomServiceFuture.send("Hello World!");
    }

    @Test
    public void close() {
        DcomServiceFuture.close();
    }
}