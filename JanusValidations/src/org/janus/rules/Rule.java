package org.janus.rules;

import org.janus.actions.Action;
import org.janus.data.DataContext;

/**
 * 
 * A ValidationRuleOrAction is a Action on the values of a DataContext or the
 * check of a rule.
 * 
 * Aktion oder Prüfung anhand der Werte eines DataCOntext
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 * @see DataContext
 * 
 */
public interface Rule extends Action, Validator {
	int getEventCount();
}
