package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.helper.DebugAssistent;

/**
 * Prüft die Länge eines Eingabefeldes
 * 
 * @author THOMAS NILL Lizenz GPLv3
 */
public class LengthRule extends OneFieldRule {
    private int min;

    private int max;

    public LengthRule() {
        super();
    }

    @Override
    public boolean isOk(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        Object o = getField(ctx);
        if (o instanceof String) {
            int l = ((String) o).length();
            return (l >= min && l <= max);
        }
        return false;
    }

    public void setMax(String max) {
        DebugAssistent.doNullCheck(max);
        this.max = Integer.parseInt(max);
    }

    public void setMin(String min) {
        DebugAssistent.doNullCheck(min);
        this.min = Integer.parseInt(min);
    }

    @Override
    public void setDefaultMessage() {
        setMessage("The length of the field " + getField() + " = $("
                + getField() + ") is not between " + min + " and " + max);
    }

}
