package om.github.buerxixi.easydcom.process.impl;

import om.github.buerxixi.easydcom.process.AbsPayloadProcess;
import om.github.buerxixi.easydcom.util.BizConstant;
import om.github.buerxixi.easydcom.util.DCOMConstant;
import om.github.buerxixi.easydcom.util.XMLUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ACKMPayloadProcess extends AbsPayloadProcess {

    private List<String> resp = new ArrayList<>();

    @Override
    public void process(String xml) {

        // ACMK 相应失败直接抛出异常
        if (BizConstant.ACKM.equals(XMLUtil.getBizSvc(xml))) {
            if (!DCOMConstant.VLDT_RST_SUCCESS.equals(XMLUtil.getVldtRst(xml))) {
                throw new RuntimeException(XMLUtil.getDesc(xml));
            }
        }

        // 添加响应数据
        resp.add(xml);

        // 分页模式需要记录
        Optional<String> pgNb = XMLUtil.parse2Optional(xml, "//PgNb");
        Optional<String> pgCnt = XMLUtil.parse2Optional(xml, "//PgCnt");
        // 非分页数据直接返回
        if (!pgCnt.isPresent() || !pgNb.isPresent()) {
            this.fun.apply(resp);
            return;
        }

        // 分页模式需要判断数据是否完整
        if (resp.size() >= Integer.parseInt(pgNb.get())) {
            this.fun.apply(resp);
            return;
        }
    }
}
