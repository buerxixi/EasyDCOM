package om.github.buerxixi.easydcom.process.impl;

import om.github.buerxixi.easydcom.process.AbsPayloadProcess;
import java.util.Collections;

public class ControlPayloadProcess extends AbsPayloadProcess {

    @Override
    public void process(String s) {
        // 避免创建不必要的 ArrayList，直接使用 Collections.singletonList
        this.fun.apply(Collections.singletonList(s));
    }
}
