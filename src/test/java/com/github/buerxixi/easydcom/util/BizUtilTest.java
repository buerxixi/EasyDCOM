package com.github.buerxixi.easydcom.util;

import org.junit.Test;

public class BizUtilTest {

    @Test
    public void genBizMsgIdr() {
        for (int i = 0; i < 10; i++) {
            System.out.println(BizUtil.genBizMsgIdr(BizConstant.LORQ));
        }
    }

    @Test
    public void genCreDt() {
        System.out.println(BizUtil.genCreDt());
    }

    @Test
    public void genHRBTXML() {
        BizUtil.appIdr = "TEST";
        BizUtil.usrIdr = "ZJB0001";
        System.out.println(BizUtil.genHRBTXML());
    }
}