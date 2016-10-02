package org.janus.rules;

import java.util.ArrayList;

import org.janus.actions.Action;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;

/**
 * Abstrakte Implementation von ValidationRuleOrAction
 * 
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleWithSubrules extends BasisValidationRule implements
        ValidationRuleListener {

    /*
     * Liste von Rules und Aktionen die auszuführen sind falls eine Bedingung
     * wahr ist
     */
    private ActionList ifTrue = null;

    /*
     * Liste von Rules und Aktionen die auszuführen sind falls eine Bedingung
     * falsch ist
     */

    private ActionList ifFalse = null;

    public ValidationRuleWithSubrules(Validator validator) {
        super(validator);
        events = new ArrayList<ValidationRuleEvent>();
    }

    public ValidationRuleWithSubrules() {
        this(null);
    }

    @Override
    public void configure(DataDescription model) {
        super.configure(model);
        if (ifTrue != null) {
            ifTrue.configure(model);
        }
        if (ifFalse != null) {
            ifFalse.configure(model);
        }
    }

    private ActionList getIfTrue() {
        if (ifTrue == null) {
            ifTrue = new ActionList();
            ifTrue.addRuleListener(this);
        }
        return ifTrue;
    }

    private ActionList getIfFalse() {
        if (ifFalse == null) {
            ifFalse = new ActionList();
            ifFalse.addRuleListener(this);
        }
        return ifFalse;
    }

    /*
     * Hinzufügen einer Validierung oder Aktion
     */
    public void addIfTrue(Action r) {
        getIfTrue().addRuleOrAction(r);
    }

    /*
     * Hinzufügen einer Validierung oder Aktion
     */
    public void addIfFalse(Action r) {
        getIfFalse().addRuleOrAction(r);
    }

    /*
     * Ausführen der Regel: Wenn es Kindaktionen gibt, werden diese ausgeführt,
     * ansonsten werden RuleEvents generiert
     */
    @Override
    public void perform(DataContext ctx) {
        boolean ok = isOk(ctx);
        if (ok) {
            if (ifTrue != null) {
                ifTrue.perform(ctx);
            }
        } else {
            if (ifFalse != null) {
                ifFalse.perform(ctx);
            } else {
                fireEvents(ctx);
            }

        }
    }

    @Override
    public void consumeEvent(ValidationRuleEvent ev, DataContext context) {
        fireEvent(context, ev);

    }
}
