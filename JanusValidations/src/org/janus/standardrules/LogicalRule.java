package org.janus.standardrules;

import java.util.Vector;

import org.janus.actions.Action;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.janus.rules.ActionList;
import org.janus.rules.BasisValidationRule;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleListener;
import org.janus.rules.ValidationRuleType;

/**
 * 
 * Logische Verknüpfung der Kinder
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public abstract class LogicalRule extends MultiFieldRule implements
		ValidationRuleListener {

	private int gesamtCount = 0;

	public LogicalRule() {
		super();
	}

	/*
	 * Schlüssel für das Anzahl-Feld im Context Diese Anzahl zählt, wie oft
	 * Kinder gefeuert haben.
	 */
	private int count = 0;

	/**
	 * Liste der Kind Validierungen oder Aktionen
	 */
	private ActionList rules = null;

	@Override
	public void configure(DataDescription model) {
		super.configure(model);
		count = model.createAnonymousHandle();
		gesamtCount = 0;
		getRules().configure(model);
		for (Action r : getRules().getRulesAndActions()) {
			if (r instanceof BasisValidationRule) {
				gesamtCount += ((BasisValidationRule) r).getEventCount();
			}
		}
	}

	/*
	 * Liste der Kinder zurückgeben
	 */
	protected ActionList getRules() {
		if (rules == null) {
			rules = new ActionList();
			rules.addRuleListener(this);
		}
		return rules;
	}

	/*
	 * Kind hinzufügen
	 */
	public void addRule(Action r) {
		DebugAssistent.doNullCheck(r);

		getRules().addRuleOrAction(r);

	}

	/*
	 * Kind entfernen
	 */
	public void removeRule(Action r) {
		DebugAssistent.doNullCheck(r);

		if (r instanceof BasisValidationRule) {
			((BasisValidationRule) r).removeRuleListener(this);
		}
		getRules().removeRuleOrAction(r);
	}

	/*
	 * Die Kinder werden ausgeführt. Dabei wird auch bestimmt, wieviele Events
	 * überhaupt auftreten können.
	 * 
	 * @see
	 * org.janus.rules.ValidationRuleOrAction#perform(test.janus.data.DataContext
	 * )
	 */
	@Override
	public void perform(DataContext ctx) {
		DebugAssistent.doNullCheck(ctx);

		// Neuer Zähler für gesendete Events
		ctx.setObject(count, new Vector<ValidationRuleEvent>());

		getRules().perform(ctx);

		Vector<ValidationRuleEvent> c = (Vector<ValidationRuleEvent>) ctx
				.getObject(count);
		if (!isOk(c.size(), gesamtCount)) {
			if (getEventCount() > 0) {
				fireEvents(ctx);
			} else {
				fireEvents(ctx, c);
			}
		}
	}

	protected abstract boolean isOk(int count2, int evCount);

	/*
	 * Zählen falls ein Event von einem Kind gefeuert wurde
	 * 
	 * @see org.janus.rules.ValidationRuleListener#consume(org.janus.rules.
	 * ValidationRuleEvent, test.janus.data.DataContext)
	 */
	@Override
	public void consumeEvent(ValidationRuleEvent ev, DataContext ctx) {
		DebugAssistent.doNullCheck(ctx, ev);

		if (ValidationRuleType.RULE.equals(ev.getType())) {
			Vector<ValidationRuleEvent> c = (Vector<ValidationRuleEvent>) ctx
					.getObject(count);
			c.add(ev);
		}
	}

}
