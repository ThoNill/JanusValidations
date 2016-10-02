/* Legal Stuff
 *
 * JANUS_VALIDATION is Open Source.
 *
 * Copyright (c) 2009 Thomas Nill.  All rights reserved.
 * E-Mail t.nill@t-online.de
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted under the terms of the 
 * GNU LESSER GENERAL PUBLIC LICENSE version 2.1 or later.
 */

package org.janus.standardrules;

import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleType;
import org.janus.rules.ValidationRuleWithSubrules;

/**
 * Basisklasse für Prüfungen mit mehreren Eingabefeldern
 * 
 * @author THOMAS
 * 
 */
public abstract class MultiFieldRule extends ValidationRuleWithSubrules {

    private String fields;

    public MultiFieldRule() {
        super();
    }

    public void addDefaultEvent(DataDescription model) {
        DebugAssistent.doNullCheck(model);

        if (fields != null && getEventCount() == 0) {
            String[] mfields = fields.split(" +");
            for (int i = 0; i < mfields.length; i++) {
                model.getHandle(mfields[i]); // initialisiere das Feld
                addEvent(new ValidationRuleEvent(ValidationRuleType.RULE,
                        getLevel(), getName(), mfields[i], getMessage()));
            }
        }
    }

    @Override
    public void configure(DataDescription model) {
        super.configure(model);
        addDefaultEvent(model);
    }

    @Override
    public void setDefaultMessage() {
        if (fields != null) {
            setMessage("A " + getLevel().name()
                    + " occured on one of the Fields " + " = [" + fields + "]");
        }
        ;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        DebugAssistent.doNullCheck(fields);
        this.fields = fields;
    }

}
