package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.util.DCOMConstant;
import org.junit.Test;

import java.util.List;

public class DcomServiceTest {

    @Test
    public void connect() throws InterruptedException {
        try {
            DcomService.connect("127.0.0.1", 60000, "TEST", "TEST", "12345678");
            DcomService.send("xml");
            DcomService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(10000);
    }

    @Test
    public void read1OK() {
        List<String> list = DcomService.connect("127.0.0.1", 7231, "TEST", "TEST", "12345678");
        System.out.println(list);
        DcomService.close();
    }

    @Test
    public void read1ERROR() {
        List<String> list = DcomService.connect("127.0.0.1", 7231, "TEST", "TEST", "12345678");
        System.out.println(list);
        DcomService.close();
    }

    @Test
    public void readList() {
        List<String> list = DcomService.connect("127.0.0.1", 7231, "TEST", "TEST", "12345678");
        System.out.println(list);
        DcomService.close();
    }

}