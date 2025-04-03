package om.github.buerxixi.easydcom.process.impl;

import io.reactivex.rxjava3.disposables.Disposable;
import om.github.buerxixi.easydcom.process.AbsPayloadProcess;
import om.github.buerxixi.easydcom.service.EventBus;

public class ConnectPayloadProcess extends AbsPayloadProcess<Void> {

    private Disposable eventPublishDisposable;


    @Override
    public void process(String xml) {

    }

    @Override
    protected void addFutureComplete() {
        eventPublishDisposable = EventBus.eventPublish.subscribe(eventMessage -> {
            if (eventMessage.getThrowable() != null) {
                future.completeExceptionally(eventMessage.getThrowable());
            } else {
                future.complete(null);
            }
        });
    }

    @Override
    public void close() {
        eventPublishDisposable.dispose();
    }
}
