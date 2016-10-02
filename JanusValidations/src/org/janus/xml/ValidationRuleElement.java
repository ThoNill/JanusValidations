package org.janus.xml;

import org.janus.actions.Action;
import org.janus.data.DefaultClassFactory;
import org.janus.rules.ValidationRuleWithSubrules;
import org.jdom.Element;

/**
 * Erzeugt ein RueOrAction Objekt aus einem XML Tag
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleElement extends TransferAttributesElement {

    private static final long serialVersionUID = 6331458373093534982L;

    public ValidationRuleElement() {
        super();
    }

    public Action getRuleOrAction() {
        String classname = getAttributeValue("class");
        Object obj = DefaultClassFactory.FACTORY.getInstance(classname,
                Action.class);
        if (obj != null) {
            setAttributValues(obj);
            if (obj instanceof ValidationRuleWithSubrules) {
                ValidationRuleWithSubrules ar = (ValidationRuleWithSubrules) obj;

                for (Object o : getChildren()) {
                    Element e = (Element) o;

                    if ("TRUE".equals(e.getName())) {
                        childsOfTrueIntoTrueList(ar, e);
                    } else if ("FALSE".equals(e.getName())) {
                        childsOfFalseIntoFalseList(ar, e);
                    } else if (e instanceof ValidationRuleElement) {
                        ar.addIfTrue(((ValidationRuleElement) e)
                                .getRuleOrAction());
                    } else {
                        throw new IllegalArgumentException(
                                "Only TRUE, FALSE, or RULE, IF and LOAD tags are allowed");
                    }
                }
            }
        }
        return (Action) obj;
    }

    private void childsOfFalseIntoFalseList(ValidationRuleWithSubrules ar,
            Element e) {
        for (Object ei : e.getChildren()) {
            ValidationRuleElement ee = (ValidationRuleElement) ei;
            ar.addIfFalse(ee.getRuleOrAction());
        }
    }

    private void childsOfTrueIntoTrueList(ValidationRuleWithSubrules ar,
            Element e) {
        for (Object ei : e.getChildren()) {
            ValidationRuleElement ee = (ValidationRuleElement) ei;
            ar.addIfTrue(ee.getRuleOrAction());
        }
    }

}
