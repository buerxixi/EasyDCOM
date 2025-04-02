package om.github.buerxixi.easydcom.service;

import org.junit.Test;

import java.util.List;

public class DcomServiceTest {

    @Test
    public void test() {
        List<String> list = DcomService.connect("127.0.0.1", 7231, "TEST", "TEST", "12345678");
        System.out.println(list);
    }

}