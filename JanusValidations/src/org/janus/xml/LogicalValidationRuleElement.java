package org.janus.xml;

import org.janus.actions.Action;
import org.janus.data.ClassFactory;
import org.janus.data.DefaultClassFactory;
import org.janus.standardrules.LogicalRule;

/**
 * Erzeugt ein RueOrAction Objekt aus einem XML Tag
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class LogicalValidationRuleElement extends ValidationRuleElement {

	private static final long serialVersionUID = 6331458373093534982L;

	public LogicalValidationRuleElement() {
		super();
	}

	@Override
	public Action getRuleOrAction() {
		String classname = getAttributeValue("class");
		Object obj = DefaultClassFactory.FACTORY.getInstance(classname, LogicalRule.class);
		if (obj != null) {
			setAttributValues(obj);
			if (obj instanceof LogicalRule) {
				LogicalRule lr = (LogicalRule) obj;
				for (Object o : getChildren()) {
					ValidationRuleElement e = (ValidationRuleElement) o;
					lr.addRule(e.getRuleOrAction());
				}
			}
		}
		return (Action) obj;
	}

}
