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
        Optional<String> pgNb = XMLUtil.parse2Optional(xml, "//PgNb"); // 当前页码
        Optional<String> pgCnt = XMLUtil.parse2Optional(xml, "//PgCnt"); // 总页数
        // 非分页数据直接返回
        if (!pgCnt.isPresent() || !pgNb.isPresent()) {
            this.fun.apply(resp);
            return;
        }

        // 分页模式需要判断数据是否完整
        if (resp.size() >= Integer.parseInt(pgCnt.get())) {

            // 按字段进行排序
            resp.sort((xml1, xml2) -> {
                // 提取 sortField 字段的值
                String field1 = XMLUtil.parse2Optional(xml1, "//PgNb").orElse("");
                String field2 = XMLUtil.parse2Optional(xml2, "//PgNb").orElse("");
                // 假设 sortField 是数字类型，将其转换为整数进行比较
                int value1 = Integer.parseInt(field1);
                int value2 = Integer.parseInt(field2);
                return Integer.compare(value1, value2);
            });

            // 按字段进行排序
            this.fun.apply(resp);
            return;
        }
    }
}
