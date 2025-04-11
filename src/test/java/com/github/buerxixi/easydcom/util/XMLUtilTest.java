package com.github.buerxixi.easydcom.util;

import org.junit.Test;

public class XMLUtilTest {

    @Test
    public void allTest() {

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
        String bizMsgIdr = XMLUtil.getBizMsgIdr(xml);
        String rltd = XMLUtil.getRltd(xml);
        String bizSvc = XMLUtil.getBizSvc(xml);

        System.out.println("BizMsgIdr: " + bizMsgIdr);
        System.out.println("BizSvc: " + bizSvc);
        System.out.println("Rltd: " + rltd);
    }
}