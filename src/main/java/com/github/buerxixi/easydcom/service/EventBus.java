package com.github.buerxixi.easydcom.service;

import io.reactivex.rxjava3.subjects.PublishSubject;
import com.github.buerxixi.easydcom.pojo.EventMessage;

public class EventBus {
    // 通知类报文
    public static final PublishSubject<String> noticePublish = PublishSubject.create();
    // 响应类报文
    public static final PublishSubject<String> respPublish = PublishSubject.create();
    // 事件
    public static final PublishSubject<EventMessage> eventPublish = PublishSubject.create();
}
