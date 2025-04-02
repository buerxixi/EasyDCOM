package om.github.buerxixi.easydcom.util;

import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

@Log4j2
public class XMLUtil {

    private static String getXPathValue(String xml, String xpathExpression) {
        String value = "";
        try {
            // 使用 SAXReader 解析 XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml.trim()));
            Node node = document.selectSingleNode(xpathExpression);
            value = node != null ? node.getText().trim() : null;
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }

        return value;
    }

    /**
     * 报文的标识符，是检查报文是否重复的依据，同一用户当日不可重复。
     *
     * @param xml xml
     * @return 报文的标识符
     */
    static String getBizMsgIdr(String xml) {
        return getXPathValue(xml, "/Msg/AppHdr/BizMsgIdr");
    }

    /**
     * 源报文的BizMsgIdr
     *
     * @param xml xml
     * @return 源报文的BizMsgIdr
     */
    static String getRltd(String xml) {
        return getXPathValue(xml, "/Msg/AppHdr/Rltd");
    }

    /**
     * 业务类型标识符
     *
     * @param xml xml
     * @return 业务类型标识符
     */
    static String getBizSvc(String xml) {
        return getXPathValue(xml, "/Msg/AppHdr/BizSvc");
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<Msg>\n" +
                "    <AppHdr>\n" +
                "        <CharSet>UTF-8</CharSet>\n" +
                "        <Fr>\n" +
                "            <AppIdr>DCOMNW</AppIdr>\n" +
                "            <UsrIdr>CSDCSZ</UsrIdr>\n" +
                "        </Fr>\n" +
                "        <To>\n" +
                "            <AppIdr>TEST</AppIdr>\n" +
                "            <UsrIdr>ZJB0001</UsrIdr>\n" +
                "        </To>\n" +
                "        <BizMsgIdr>M20150813LIRP00000000001</BizMsgIdr>\n" +
                "        <MsgDefIdr>V2.0</MsgDefIdr>\n" +
                "        <BizSvc>LIRP</BizSvc>\n" +
                "        <CreDt>2015-08-13T12:00:34</CreDt>\n" +
                "        <Rltd>M20150813LIRQ00000000001</Rltd>\n" +
                "    </AppHdr>\n" +
                "    <Document>\n" +
                "        <UserName>TEST</UserName>\n" +
                "        <VldtRst>0000</VldtRst>\n" +
                "        <Desc>处理成功</Desc>\n" +
                "    </Document>\n" +
                "</Msg>";
        String bizMsgIdr = getBizMsgIdr(xml);
        String rltd = getRltd(xml);
        String bizSvc = getBizSvc(xml);

        System.out.println("BizMsgIdr: " + bizMsgIdr);
        System.out.println("BizSvc: " + bizSvc);
        System.out.println("Rltd: " + rltd);
    }
}
