package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.data.Message;
import org.janus.helper.DebugAssistent;

public abstract class TwoFieldRule extends MultiFieldRule {

    private String a;
    private String b;
    private Message va;
    private Message vb;

    public TwoFieldRule() {
        super();
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public void configure(DataDescription model) {
        super.configure(model);
        va = createMessage(model, a);
        vb = createMessage(model, b);
    }

    @Override
    public boolean isOk(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        Object oa = va.getMessage(ctx);
        Object ob = vb.getMessage(ctx);
        return isOk(oa, ob, ctx);
    }

    protected abstract boolean isOk(Object oa, Object ob, DataContext ctx);

}