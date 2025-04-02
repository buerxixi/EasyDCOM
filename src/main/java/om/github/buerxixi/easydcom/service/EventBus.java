package om.github.buerxixi.easydcom.service;

import io.reactivex.rxjava3.subjects.PublishSubject;
import om.github.buerxixi.easydcom.pojo.EventMessage;

public class EventBus {
    // 所有消息队列
    public static final PublishSubject<String> publish = PublishSubject.create();
    // 事件
    public static final PublishSubject<EventMessage> eventPublish = PublishSubject.create();
}
