package org.janus.rules;

import java.util.Vector;

import org.janus.data.DataDescriptionImpl;
import org.janus.helper.DebugAssistent;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class RuleDescription extends DataDescriptionImpl {

	private Vector<ValidationRuleEvent> events = new Vector<>();

	public RuleDescription() {
		// TODO Auto-generated constructor stub
	}

	public void registerEvent(ValidationRuleEvent ev) {
		DebugAssistent.doNullCheck(ev);

		ev.setIndex(events.size());
		events.add(ev);
	}

	public int getEventCount() {
		return events.size();
	}

	public ValidationRuleEvent getEvent(int index) {
		return events.get(index);
	}
}
