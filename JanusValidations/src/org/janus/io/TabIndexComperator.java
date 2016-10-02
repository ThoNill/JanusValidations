package org.janus.io;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

import org.janus.helper.DebugAssistent;
import org.janus.rules.ValidationRuleEvent;

/**
 * Der TabIndexComperator vergleicht zwei ValidationRuleEvents über Ihre
 * Position in einer Tabulatorreihe.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 * @see ValidationRuleEvent
 * @see Stepper
 * 
 */

public class TabIndexComperator extends HashMap<String, StepperHelper>
        implements Comparator<ValidationRuleEvent>, Serializable {

    private static final long serialVersionUID = -8834000984547797107L;

    @Override
    public int compare(ValidationRuleEvent e1, ValidationRuleEvent e2) {
        DebugAssistent.doNullCheck(e1, e2);

        StepperHelper h1 = get(e1.getPosition());
        StepperHelper h2 = get(e2.getPosition());
        return (h1.getTabindex() - h2.getTabindex());
    }

}
