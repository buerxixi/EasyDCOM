package om.github.buerxixi.easydcom.util;

import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;

@Log4j2
public class BizUtil {

    public static String appIdr;
    public static String usrIdr;
    public static String password;

    /**
     * 获取从当天 0 点开始到当前时刻的秒数
     *
     * @return 毫秒数
     */
    public static long getSecondsSinceMidnight() {
        // 获取北京时区的当前日期时间
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 获取北京时区当天 0 点的时间
        ZonedDateTime midnight = now.with(LocalTime.MIDNIGHT);
        // 计算从当天 0 点到当前时刻的毫秒数
        return now.toInstant().toEpochMilli() - midnight.toInstant().toEpochMilli();
    }

    /**
     * 生成报文ID
     *
     * @param biz 业务类型
     * @return 报文ID
     */
    public static String genBizMsgIdr(String biz) {
        // 获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());

        // 生成序号
        long sequence = getSecondsSinceMidnight();

        // 随机数
        Random random = new Random();
        int salt = random.nextInt(10000);

        // 拼接报文ID
        return String.format("%s%s%s%07d%04d", "M", date, biz, sequence, salt);
    }

    /**
     * 报文的创建时间
     *
     * @return 报文的创建时间
     */
    public static String genCreDt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 生成报文
     *
     * @param appIdr               应用系统标识符
     * @param usrIdr               用户标识/用户名(UserName)
     * @param biz                  业务类型
     * @param documentElemConsumer 业务报文
     * @return 报文
     */
    public static String genAppHdr(String appIdr, String usrIdr, String biz, Consumer<Element> documentElemConsumer) {


        // 将 XML 写入字符串
        StringWriter stringWriter = new StringWriter();
        try {
            // 1. 创建文档和根节点
            Document document = DocumentHelper.createDocument();
            Element msgRoot = document.addElement("Msg");

            // 2. 构建 AppHdr 部分
            Element appHdr = msgRoot.addElement("AppHdr");
            appHdr.addElement("CharSet").addText("UTF-8");

            // 构建 Fr 节点
            Element fr = appHdr.addElement("Fr");
            fr.addElement("AppIdr").addText(appIdr);
            fr.addElement("UsrIdr").addText(usrIdr);


            // 构建 To 节点
            Element to = appHdr.addElement("To");
            to.addElement("AppIdr").addText("DCOMNW");
            to.addElement("UsrIdr").addText("CSDCSZ");

            // 其他 AppHdr 元素
            appHdr.addElement("BizMsgIdr").addText(genBizMsgIdr(biz));
            appHdr.addElement("MsgDefIdr").addText("V2.0");
            appHdr.addElement("BizSvc").addText(biz);
            appHdr.addElement("CreDt").addText(genCreDt());

            // 3. 构建 Document 部分
            Element documentElem = msgRoot.addElement("Document");
            if (documentElemConsumer != null) {
                documentElemConsumer.accept(documentElem);
            }


            // 使用紧凑格式（无缩进和换行）
            OutputFormat format = OutputFormat.createCompactFormat();
            // 4. 设置 XML 声明和格式化
            // OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setSuppressDeclaration(false); // 包含 XML 声明

            // 5. 将文档写入字符串
            XMLWriter writer = new XMLWriter(stringWriter, format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return stringWriter.toString();
    }

    /**
     * 生成报文
     *
     * @param biz                  业务类型
     * @param documentElemConsumer 业务报文
     * @return 报文
     */
    public static String genAppHdr(String biz, Consumer<Element> documentElemConsumer) {
        return genAppHdr(appIdr, usrIdr, biz, documentElemConsumer);
    }


    /**
     * 登录请求报文
     */
    public static String genLIRQXML(String appIdr, String usrIdr, String password, Integer recvHB) {
        BizUtil.appIdr = appIdr;
        BizUtil.usrIdr = usrIdr;
        BizUtil.password = password;
        return genAppHdr(BizConstant.LIRQ, documentElem -> {
            documentElem.addElement("UserName").addText(appIdr);
            documentElem.addElement("Password").addText(password);
            documentElem.addElement("RecvHB").addText(String.valueOf(recvHB));
        });
    }


    /**
     * 注销请求报文
     */
    public static String genLORQXML() {
        return genAppHdr(BizConstant.LORQ, documentElem -> {
            documentElem.addElement("UserName").addText(appIdr);
            documentElem.addElement("Password").addText(password);
        });
    }

    /**
     * 心跳报文
     */
    public static String genHRBTXML() {
        return genAppHdr(BizConstant.HRBT, null);
    }
}
