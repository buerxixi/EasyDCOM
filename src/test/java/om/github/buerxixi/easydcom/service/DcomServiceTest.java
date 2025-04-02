package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.util.DCOMConstant;
import org.junit.Test;

import java.util.List;

public class DcomServiceTest {

    @Test
    public void connect() {
        List<String> list = DcomService.connect("127.0.0.1", 7231, "TEST", "TEST", "12345678");
        System.out.println(list);
        DcomService.close();
    }

    @Test
    public void read1() {
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