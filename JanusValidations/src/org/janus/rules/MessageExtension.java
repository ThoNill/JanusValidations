package org.janus.rules;

import org.janus.data.DataContext;
import org.janus.data.MessageImpl;
import org.janus.helper.DebugAssistent;
import org.janus.message.MessageSource;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class MessageExtension extends EventExtension<MessageImpl> {

    private String resourceName;

    public MessageExtension(RuleDescription desc) {
        super(desc);
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        DebugAssistent.doNullCheck(resourceName);

        this.resourceName = resourceName;
    }

    @Override
    protected MessageImpl createDataForEvent(ValidationRuleEvent ev) {
        DebugAssistent.doNullCheck(ev);

        String message = ev.getMessage();
        if (resourceName != null) {
            message = MessageSource.getText(resourceName, message);
        }
        return new MessageImpl(message);
    }

    public String getMessage(ValidationRuleEvent ev, DataContext ctx) {
        DebugAssistent.doNullCheck(ctx, ev);

        if (ev.getType() == ValidationRuleType.RULE) {
            configure(ctx.getDataDescription());
            return getData(ev).getMessage(ctx);
        } else {
            return "";
        }
    }
}
