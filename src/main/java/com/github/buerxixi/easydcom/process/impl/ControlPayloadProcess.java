package com.github.buerxixi.easydcom.process.impl;

import io.reactivex.rxjava3.disposables.Disposable;
import com.github.buerxixi.easydcom.process.AbsPayloadProcess;
import com.github.buerxixi.easydcom.service.EventBus;
import com.github.buerxixi.easydcom.util.XMLUtil;
import java.util.Collections;
import java.util.List;

public class ControlPayloadProcess extends AbsPayloadProcess<List<String>> {

    private Disposable eventPublishDisposable;
    private Disposable respPublishDisposable;
    private String message;


    @Override
    protected void addFutureComplete() {
        eventPublishDisposable = EventBus.eventPublish.subscribe(eventMessage -> {
            if (eventMessage.getThrowable() != null) {
                future.completeExceptionally(eventMessage.getThrowable());
            }
        });
        respPublishDisposable = EventBus.respPublish.subscribe(xml -> {
            if (XMLUtil.getBizMsgIdr(message).equals(XMLUtil.getRltd(xml))) {
                future.complete(Collections.singletonList(xml));
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
