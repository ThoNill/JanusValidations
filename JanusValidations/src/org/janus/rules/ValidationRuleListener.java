package org.janus.rules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;

/**
 * Verarbeitet ValidationRuleEvents (Ausgabe, filtern und weiterleiten,
 * Positionierung auf der GUI usw.)
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public interface ValidationRuleListener {
    void configure(DataDescription model);

    void consumeEvent(ValidationRuleEvent ev, DataContext context);
}
