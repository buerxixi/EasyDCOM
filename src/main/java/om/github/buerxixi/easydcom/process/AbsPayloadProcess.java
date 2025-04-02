package om.github.buerxixi.easydcom.process;

import java.util.List;
import java.util.function.Function;

public abstract class AbsPayloadProcess {

    protected Function<List<String>, Boolean> fun;

    public abstract void process(String s);

    public void complete(Function<List<String>, Boolean> fun) {
        this.fun = fun;
    }
}
