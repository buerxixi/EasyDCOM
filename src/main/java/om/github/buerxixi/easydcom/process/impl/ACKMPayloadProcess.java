package om.github.buerxixi.easydcom.process.impl;

import io.reactivex.rxjava3.disposables.Disposable;
import om.github.buerxixi.easydcom.exception.DCOMException;
import om.github.buerxixi.easydcom.process.AbsPayloadProcess;
import om.github.buerxixi.easydcom.service.EventBus;
import om.github.buerxixi.easydcom.util.BizConstant;
import om.github.buerxixi.easydcom.util.DCOMConstant;
import om.github.buerxixi.easydcom.util.XMLUtil;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ACKMPayloadProcess extends AbsPayloadProcess<List<String>> {

    // 将 resp 字段声明为 final
    private final List<String> resp = new CopyOnWriteArrayList<>();

    private Disposable eventPublishDisposable;
    private Disposable respPublishDisposable;
    private String message;


    private void processResp(String xml) {
        // ACMK 相应失败直接抛出异常
        if (BizConstant.ACKM.equals(XMLUtil.getBizSvc(xml))) {
            if (!DCOMConstant.VLDT_RST_SUCCESS.equals(XMLUtil.getVldtRst(xml))) {
                future.completeExceptionally(new DCOMException(XMLUtil.getDesc(xml)));
                return;
            }
        }

        // 添加响应数据
        resp.add(xml);

        // 分页模式需要记录
        Optional<String> pgNb = XMLUtil.parse2Optional(xml, "//PgNb"); // 当前页码
        Optional<String> pgCnt = XMLUtil.parse2Optional(xml, "//PgCnt"); // 总页数
        // 非分页数据直接返回
        if (!pgCnt.isPresent() || !pgNb.isPresent()) {
            future.complete(resp);
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
            future.complete(resp);
            return;
        }
    }

    @Override
    protected void addFutureComplete() {
        eventPublishDisposable = EventBus.eventPublish.subscribe(eventMessage -> {
            if (eventMessage.getThrowable() != null) {
                future.completeExceptionally(eventMessage.getThrowable());
            }
        });
        respPublishDisposable = EventBus.respPublish.subscribe(xml -> {
            if (XMLUtil.getBizMsgIdr(message).equals(XMLUtil.getRltd(xml))) {
                processResp(xml);
            }
        });
    }

    @Override
    public void close() {
        eventPublishDisposable.dispose();
        respPublishDisposable.dispose();
    }

    @Override
    public void process(String message) {
        this.message = message;
    }
}
