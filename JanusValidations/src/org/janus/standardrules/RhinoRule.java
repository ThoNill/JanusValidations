package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleType;
import org.janus.rules.ValidationRuleWithSubrules;

/**
 * Prüft ob ein Javascript Audruck Wahr zurückgibt. Zahlwerte > 0 werden als
 * Wahr angesehen.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class RhinoRule extends ValidationRuleWithSubrules {

    private RhinoConnector rhino;
    private String[] moveTo = null;

    public RhinoRule() {
        rhino = new RhinoConnector();
    }

    public void setMove(String moveTo) {
        this.moveTo = moveTo.split(" *[,]* *");
    }

    public void setScript(String scriptText) {
        rhino.setScript(scriptText);
    }

    @Override
    public void configure(DataDescription model) {
        super.configure(model);
        rhino.configure(model);
        addDefaultEvent();
    }

    @Override
    public boolean isOk(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        Object obj = rhino.getObject(ctx);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        ;
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue() > 0;
        }
        ;
        if (obj instanceof Long) {
            return ((Long) obj).longValue() > 0;
        }
        ;
        if (obj instanceof Float) {
            return ((Float) obj).floatValue() > 0;
        }
        ;
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue() > 0;
        }
        ;
        String sObj = obj.toString();
        if ("1".equals(sObj) || "true".equals(sObj) || "TRUE".equals(sObj)) {
            return true;
        }
        ;
        return false;
    }

    public void addDefaultEvent() {
        if (moveTo != null) {
            for (int i = 0; i < moveTo.length; i++) {
                addEvent(new ValidationRuleEvent(ValidationRuleType.RULE,
                        getLevel(), getName(), moveTo[i], getMessage()));
            }
        }
    }

    @Override
    public void setDefaultMessage() {
        setMessage("A " + getLevel().name() + " occured in a RhinoRule ["
                + rhino.getScriptText() + "]");
    }
}
