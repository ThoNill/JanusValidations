package org.janus.xml;

import org.apache.log4j.Logger;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleMaschine;
import org.jdom.DefaultJDOMFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * 
 * Objektfabrik für die JDOM Umgebung
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleElementFactory extends DefaultJDOMFactory {
	static Logger logger = Logger.getLogger(ValidationRuleElementFactory.class);

	public ValidationRuleElementFactory() {
		super();
	}

	@Override
	public Element element(String name) {
		Element elem = null;

		if ("RULES".equals(name)) {
			elem = new ValidationRuleListElement();
		} else if ("ACTION".equals(name)) {
			elem = new ValidationRuleElement();
		} else if ("LOAD".equals(name)) {
			elem = new ValidationRuleElement();
		} else if ("RULE".equals(name)) {
			elem = new ValidationRuleElement();
		} else if ("IF".equals(name)) {
			elem = new ValidationRuleElement();
			elem.setAttribute("class", "org.janus.standardrules.RegExpRule");
		} else if ("TRUE".equals(name)) {
			elem = super.element(name);
		} else if ("FALSE".equals(name)) {
			elem = super.element(name);
		} else if ("NOT".equals(name)) {
			elem = new LogicalValidationRuleElement();
			elem.setAttribute("class", "org.janus.standardrules.NotRule");
		} else if ("AND".equals(name)) {
			elem = new LogicalValidationRuleElement();
			elem.setAttribute("class", "org.janus.standardrules.AndRule");
		} else if ("OR".equals(name)) {
			elem = new LogicalValidationRuleElement();
			elem.setAttribute("class", "org.janus.standardrules.OrRule");
		} else if ("LISTENER".equals(name)) {
			elem = new ValidationRuleListenerElement();
		} else {
			throw new RuntimeException("Tag is not allowed");
		}
		elem.setName(name);
		return elem;
	}

	public static ValidationRuleMaschine createRuleList(
			RuleDescription description, String dir, String filename)
			throws Exception {
		if (dir == null || filename == null) {
			throw new NullPointerException();
		}

		try {
			ValidationRuleElementFactory factory = new ValidationRuleElementFactory();
			SAXBuilder builder = new SAXBuilder();
			builder.setFactory(factory);
			Document doc = builder.build(dir + filename);
			ValidationRuleListElement r = (ValidationRuleListElement) doc
					.getRootElement();
			ValidationRuleMaschine rules = r.getRuleList(description);
			rules.configure();
			return rules;

		} catch (Exception ex) {
			logger.fatal("cannot create Document " + dir + filename + " ["
					+ ex.getMessage() + "]");
			throw ex;
		}
	}
}
