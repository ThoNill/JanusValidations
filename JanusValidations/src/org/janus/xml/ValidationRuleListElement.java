package org.janus.xml;

import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleMaschine;
import org.jdom.Element;

/**
 * Erzeugt eine ValidationRuleMaschine aus mehreren XML Tags.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleListElement extends Element {

    private static final long serialVersionUID = -2551566740514374362L;

    public ValidationRuleListElement() {
        super();
    }

    public ValidationRuleMaschine getRuleList(RuleDescription description) {
        ValidationRuleMaschine ruleList = new ValidationRuleMaschine(
                description);
        for (Object o : getChildren()) {
            Element e = (Element) o;
            if (e instanceof ValidationRuleElement) {
                ruleList.addRuleOrAction(((ValidationRuleElement) e)
                        .getRuleOrAction());
            } else if (e instanceof ValidationRuleListenerElement) {
                ruleList.addRuleListener(((ValidationRuleListenerElement) e)
                        .getRuleListener());
            } else {
                throw new IllegalArgumentException(
                        "Only RULE, IF, LOAD and LISTENER tags are allowed");
            }
        }
        return ruleList;
    }
}
