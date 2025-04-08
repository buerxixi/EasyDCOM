package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.util.DCOMConstant;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

public class DcomServiceTest {

    @Test
    public void connect() throws InterruptedException {
        try {
            DcomService.connect("127.0.0.1", 60000, "TEST", "TEST", "12345678");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            DcomService.send("<?xmlversion=\"1.0\"encoding=\"UTF-8\"?><Msg><AppHdr><CharSet>UTF-8</CharSet><Fr><AppIdr>DCOMNW</AppIdr><UsrIdr>CSDCSZ</UsrIdr></Fr><To><AppIdr>_ALL_SYS</AppIdr><UsrIdr>ZJB0001</UsrIdr></To><BizMsgIdr>M20151225TZXX00000000024</BizMsgIdr><MsgDefIdr>V2.0</MsgDefIdr><BizSvc>TZXX</BizSvc><CreDt>2016-02-15T16:01:18</CreDt></AppHdr><Document><Data><NtcTtl>测试通知信息</NtcTtl><NtcCntnt>测试通知信息具体内容</NtcCntnt><NtcTm>2016-02-15T15:13:23</NtcTm></Data></Document></Msg>");
            DcomService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read1OK() throws InterruptedException {
        List<String> list = DcomService.connect("127.0.0.1", 7231, "TEST", "TEST", "12345678");
        System.out.println(list);
        DcomService.close();
        Thread.sleep(10000);
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