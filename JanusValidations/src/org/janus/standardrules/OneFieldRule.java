package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleType;
import org.janus.rules.ValidationRuleWithSubrules;
import org.janus.rules.Validator;

/**
 * Basisklasse für Prüfungen mit einem Eingabefeld
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public abstract class OneFieldRule extends ValidationRuleWithSubrules {

	private String field;

	private int iField;

	public OneFieldRule(Validator validator) {
		super(validator);
	}

	public OneFieldRule() {
		super();
	}

	public void addDefaultEvent() {
		if (getEventCount() == 0) {
			addEvent(new ValidationRuleEvent(ValidationRuleType.RULE,
					getLevel(), getName(), getField(), getMessage()));
		}
	}

	@Override
	public void configure(DataDescription model) {
		super.configure(model);
		addDefaultEvent();
		iField = model.getHandle(field);
	}

	@Override
	public void setDefaultMessage() {
		setMessage("A " + getLevel().name() + " occured on the Field " + field
				+ " = [$(" + field + ")]");
	}

	public Object getField(DataContext ctx) {
		DebugAssistent.doNullCheck(ctx);

		return ctx.getObject(iField);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		DebugAssistent.doNullCheck(field);

		this.field = field;
	}

}
