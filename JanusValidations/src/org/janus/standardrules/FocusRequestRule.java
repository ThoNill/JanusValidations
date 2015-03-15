package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.rules.ValidationLevel;

/**
 * Setzt den Fokus auf ein Eingabefeld
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class FocusRequestRule extends OneFieldRule {

	public FocusRequestRule() {
		super();
		setLevel(ValidationLevel.FOCUS);
	}

	@Override
	public boolean isOk(DataContext ctx) {
		return false;
	}

	@Override
	public void setDefaultMessage() {
		setMessage("Focus request from " + getField());
	}

}
