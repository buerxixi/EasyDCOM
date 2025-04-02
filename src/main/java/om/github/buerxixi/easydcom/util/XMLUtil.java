package om.github.buerxixi.easydcom.util;

import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

@Log4j2
public class XMLUtil {

    public static String getXPathValue(String xml, String xpathExpression) {
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
    public static String getBizMsgIdr(String xml) {
        return getXPathValue(xml, "/Msg/AppHdr/BizMsgIdr");
    }

    /**
     * 源报文的BizMsgIdr
     *
     * @param xml xml
     * @return 源报文的BizMsgIdr
     */
    public static String getRltd(String xml) {
        return getXPathValue(xml, "/Msg/AppHdr/Rltd");
    }

    /**
     * 业务类型标识符
     *
     * @param xml xml
     * @return 业务类型标识符
     */
    public static String getBizSvc(String xml) {
        return getXPathValue(xml, "/Msg/AppHdr/BizSvc");
    }

}
