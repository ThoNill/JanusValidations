package org.janus.rules;

import java.util.ArrayList;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;

public class RuleListenerImpl implements ValidationRuleListener {

	private ArrayList<ValidationRuleListener> listeners = null;

	public RuleListenerImpl() {
		super();
	}

	@Override
	public void configure(DataDescription model) {
		DebugAssistent.doNullCheck(model);

		if (listeners != null) {
			for (ValidationRuleListener r : listeners) {
				r.configure(model);
			}
		}
	}

	public void addRuleListener(ValidationRuleListener l) {
		DebugAssistent.doNullCheck(l);

		if (l == null) {
			throw new IllegalArgumentException("Übergabe eines Null-Listeners");
		}
		if (listeners == null) {
			listeners = new ArrayList<ValidationRuleListener>();
		}
		listeners.add(l);
	}

	public void removeRuleListener(ValidationRuleListener l) {
		DebugAssistent.doNullCheck(l);

		if (listeners != null) {
			listeners.remove(l);
		}
	}

	@Override
	public void consumeEvent(ValidationRuleEvent ev, DataContext ctx) {
		DebugAssistent.doNullCheck(ctx, ev);

		if (listeners != null) {
			for (ValidationRuleListener l : listeners) {
				l.consumeEvent(ev, ctx);
			}
		}
	}

}