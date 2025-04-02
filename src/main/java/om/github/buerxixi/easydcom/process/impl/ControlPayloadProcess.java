package om.github.buerxixi.easydcom.process.impl;

import om.github.buerxixi.easydcom.process.AbsPayloadProcess;

import java.util.ArrayList;

public class ControlPayloadProcess extends AbsPayloadProcess {

    @Override
    public void process(String s) {
        this.fun.apply(new ArrayList<>());
    }
}
