package org.janus.xml;

import org.janus.data.ClassFactory;
import org.janus.data.DefaultClassFactory;
import org.janus.rules.ValidationRuleListener;
import org.janus.rules.ValidationRuleListenerList;

/**
 * Erzeugt einen ValidationRuleListener aus einem XML Tag
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleListenerElement extends TransferAttributesElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 303641869971579279L;

	public ValidationRuleListenerElement() {
		super();
	}

	public ValidationRuleListener getRuleListener() {
		String classname = getAttributeValue("class");
		System.out.println(classname);
		Object obj = DefaultClassFactory.FACTORY.getInstance(classname,
				ValidationRuleListener.class);
		if (obj != null) {
			setAttributValues(obj);
			if (obj instanceof ValidationRuleListenerList) {
				ValidationRuleListenerList ar = (ValidationRuleListenerList) obj;

				for (Object iobj : getChildren()) {
					if (iobj instanceof ValidationRuleListenerElement) {
						ValidationRuleListenerElement l = (ValidationRuleListenerElement) iobj;
						ar.addRuleListener(l.getRuleListener());
					} else {
						throw new RuntimeException(
								"Only LISTENER Tags ar allowed");
					}
				}
			} else {
				if (getChildren().size() > 0) {
					throw new RuntimeException("No subtags allowed");
				}
			}
		}
		return (ValidationRuleListener) obj;
	}
}
