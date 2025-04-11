package com.github.buerxixi.easydcom.util;

import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import java.io.StringReader;
import java.util.Optional;

@Log4j2
public class XMLUtil {

    static public Optional<String> parse2Optional(String xml, String xpathExpression) {
        try {
            // 使用 SAXReader 解析 XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml.trim()));
            Node node = document.selectSingleNode(xpathExpression);
            if (node != null) {
                return Optional.of(node.getText().trim());
            }
        } catch (DocumentException e) {
            log.error("error xml\n{}", xml);
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static String parse2text(String xml, String xpathExpression) {
        Optional<String> soptional = parse2Optional(xml, xpathExpression);
        return soptional.orElse("");
    }

    /**
     * 报文的标识符，是检查报文是否重复的依据，同一用户当日不可重复。
     *
     * @param xml xml
     * @return 报文的标识符
     */
    public static String getBizMsgIdr(String xml) {
        return parse2text(xml, "/Msg/AppHdr/BizMsgIdr");
    }

    /**
     * 源报文的BizMsgIdr
     *
     * @param xml xml
     * @return 源报文的BizMsgIdr
     */
    public static String getRltd(String xml) {
        return parse2text(xml, "/Msg/AppHdr/Rltd");
    }

    /**
     * 业务类型标识符
     *
     * @param xml xml
     * @return 业务类型标识符
     */
    public static String getBizSvc(String xml) {
        return parse2text(xml, "/Msg/AppHdr/BizSvc");
    }

    /**
     * 校验结果
     *
     * @param xml xml
     * @return 校验结果
     */
    public static String getVldtRst(String xml) {
        return parse2text(xml, "/Msg/Document/VldtRst");
    }

    /**
     * 校验结果描述
     *
     * @param xml xml
     * @return 校验结果描述
     */
    public static String getDesc(String xml) {
        return parse2text(xml, "/Msg/Document/Desc");
    }

}
