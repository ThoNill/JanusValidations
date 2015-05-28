package org.janus.rules;

import org.janus.data.Configurable;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public abstract class EventExtension<K> implements Configurable {
	private Object extensionData[];

	public EventExtension(RuleDescription description) {
		configure(description);
	}

	public EventExtension() {
	}

	protected abstract K createDataForEvent(ValidationRuleEvent ev);

	@Override
	public void configure(DataDescription description) {
		DebugAssistent.doNullCheck(description);

		RuleDescription ruleDescription = (RuleDescription) description;
		int eventCount = ruleDescription.getEventCount();
		if (extensionData == null || eventCount > extensionData.length) {
			extensionData = new Object[eventCount];
			for (int i = 0; i < eventCount; i++) {
				extensionData[i] = createDataForEvent(ruleDescription
						.getEvent(i));
			}
		}
	}

	public int getEventCount() {
		return extensionData.length;
	}

	@SuppressWarnings("unchecked")
	public K getData(ValidationRuleEvent ev) {
		DebugAssistent.doNullCheck(ev);
		return (K) extensionData[ev.getIndex()];
	}

	@SuppressWarnings("unchecked")
	public K getData(int index) {
		return (K) extensionData[index];
	}

}
