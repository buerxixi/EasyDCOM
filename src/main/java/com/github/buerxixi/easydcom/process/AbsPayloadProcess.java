package com.github.buerxixi.easydcom.process;

import java.util.concurrent.CompletableFuture;

public abstract class AbsPayloadProcess<T> {

    protected CompletableFuture<T> future;

    public Runnable addFuture(CompletableFuture<T> future) {
        this.future = future;

        // 返回的数据 如果调用 会调用close方法
        addFutureComplete();
        return this::close;
    }

    abstract protected void addFutureComplete();

    abstract public void close();

    abstract public void process(String message);
}
