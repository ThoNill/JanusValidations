package org.janus.rules;

import org.apache.log4j.Logger;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;

/**
 * Mit einem ValidationRuleEventLogger werden ValidationRuleEvents
 * protokolliert.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 * @see ValidationRuleEvent
 * 
 */

public class ValidationRuleEventLogger implements ValidationRuleListener {

	private MessageExtension messageExtension;
	private String resourceName;

	public ValidationRuleEventLogger() {
		super();
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		DebugAssistent.doNullCheck(resourceName);
		this.resourceName = resourceName;
	}

	public MessageExtension getMessageExtension() {
		return messageExtension;
	}

	public void setMessageExtension(MessageExtension messageExtension) {
		DebugAssistent.doNullCheck(messageExtension);
		this.messageExtension = messageExtension;
	}

	static Logger log = Logger.getLogger(ValidationRuleEventLogger.class
			.getSimpleName());

	@Override
	public void consumeEvent(ValidationRuleEvent ev, DataContext context) {
		DebugAssistent.doNullCheck(ev, context);

		if (ValidationLevel.FOCUS.equals(ev.getLevel())) {
			log.info(messageExtension.getMessage(ev, context));
		} else if (ValidationLevel.WARNING.equals(ev.getLevel())) {
			log.debug(messageExtension.getMessage(ev, context));
		} else if (ValidationLevel.ERROR.equals(ev.getLevel())) {
			log.error(messageExtension.getMessage(ev, context));
		}
		if (ValidationLevel.FATAL.equals(ev.getLevel())) {
			log.fatal(messageExtension.getMessage(ev, context));
		}
		context.debug(log);
	}

	@Override
	public void configure(DataDescription description) {
		DebugAssistent.doNullCheckAndOfType(RuleDescription.class, description);

		RuleDescription ruleDescription = (RuleDescription) description;

		MessageExtension messageExtension = new MessageExtension(
				ruleDescription);
		if (resourceName != null) {
			messageExtension.setResourceName(resourceName);
		}
		setMessageExtension(messageExtension);
	}

}
