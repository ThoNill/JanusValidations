package org.janus.rules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;

/**
 * Mehrere Regeln in einer Liste zusammengefasst
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleMaschine extends ActionList {

	private RuleDescription description;

	private static final ValidationRuleEvent init = new ValidationRuleEvent(
			ValidationRuleType.INIT, ValidationLevel.FOCUS, "rule",
			"noPosition", "init");

	private static final ValidationRuleEvent start = new ValidationRuleEvent(
			ValidationRuleType.START, ValidationLevel.FOCUS, "rule",
			"noPosition", "start");

	private static final ValidationRuleEvent stop = new ValidationRuleEvent(
			ValidationRuleType.STOP, ValidationLevel.FOCUS, "rule",
			"noPosition", "stop");

	public ValidationRuleMaschine(RuleDescription description) {
		super();
		DebugAssistent.doNullCheck(description);
		this.description = description;
	}

	public void init(DataContext context) {
		DebugAssistent.doNullCheck(context);

		consumeEvent(init, context);
	}

	@Override
	public void perform(DataContext context) {
		DebugAssistent.doNullCheck(context);

		consumeEvent(start, context);
		super.perform(context);
		consumeEvent(stop, context);
	}

	@Override
	public void configure(DataDescription model) {
		DebugAssistent.doNullCheck(model);

		configureRulesAndActions(model);
		configureListeners(model);
	}

	public void configure() {
		configure(description);
	}
}
