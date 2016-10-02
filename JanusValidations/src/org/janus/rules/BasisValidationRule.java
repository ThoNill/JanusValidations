package org.janus.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.data.Message;
import org.janus.data.MessageImpl;
import org.janus.helper.DebugAssistent;

/**
 * Abstrakte Implementation von ValidationRuleOrAction Dient als abstrakte Basis
 * von Validierungsregeln
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class BasisValidationRule implements Rule {
    private static final String UNERWARTETE_AUSNAHME = "unerwartete Ausnahme";
    static private final Logger LOG = Logger
            .getLogger(BasisValidationRule.class);

    private RuleDescription description;
    private Validator validator;

    public BasisValidationRule(Validator validator) {
        super();
        this.validator = validator;

    }

    public BasisValidationRule() {
        this(null);
    }

    public RuleDescription getDescription() {
        return description;
    }

    public void setDescription(DataDescription description) {
        DebugAssistent.doNullCheckAndOfType(RuleDescription.class, description);
        this.description = (RuleDescription) description;
    }

    /*
     * Die Listener auf diese Regel
     */
    ArrayList<ValidationRuleListener> listeners = null;

    /*
     * Die von dieser Regel versendeten Events
     */
    protected ArrayList<ValidationRuleEvent> events = null;

    /*
     * Der Level der Validierung
     */
    private ValidationLevel level = ValidationLevel.ERROR;

    /*
     * Der Name der Regel
     */
    private String name = null;

    /**
     * Die Fehlermeldung zur Regel
     */
    private String message = null;

    protected Message createMessage(DataDescription model, String text) {
        DebugAssistent.doNullCheck(model, text);

        MessageImpl m = new MessageImpl(text);
        m.initHandles(model);
        return m;
    }

    /**
     * Die Prüfung oder Aktion konfigurieren
     */
    @Override
    public void configure(DataDescription description) {
        DebugAssistent.doNullCheck(description);

        setDescription(description);
        if (validator != null) {
            validator.configure(description);
        }
    }

    /*
     * Ein ValidationEvent hinzufügen
     */
    public void addEvent(ValidationRuleEvent ev) {
        DebugAssistent.doNullCheck(ev);

        getEvents().add(ev);
        description.registerEvent(ev);
    }

    private ArrayList<ValidationRuleEvent> getEvents() {
        if (events == null) {
            events = new ArrayList<ValidationRuleEvent>();
        }
        return events;
    }

    /*
     * Die Liste der Lauschern zurückgeben
     */
    private ArrayList<ValidationRuleListener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<ValidationRuleListener>();
        }
        return listeners;
    }

    /*
     * Einen Lauscher hinzufügen
     */
    public void addRuleListener(ValidationRuleListener l) {
        DebugAssistent.doNullCheck(l);

        getListeners().add(l);
    }

    /*
     * Einen Lauscher entfernen
     */
    public void removeRuleListener(ValidationRuleListener l) {
        DebugAssistent.doNullCheck(l);

        getListeners().remove(l);
    }

    /*
     * Ein Ereignis an die Lauscher senden
     */
    public void fireEvents(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        fireEvents(ctx, getEvents());
    }

    public void fireEvents(DataContext ctx, List<ValidationRuleEvent> events) {
        DebugAssistent.doNullCheck(ctx);

        if (listeners == null) {
            return;
        }
        for (ValidationRuleEvent e : events) {
            fireEvent(ctx, e);
        }
    }

    public void fireEvent(DataContext ctx, ValidationRuleEvent e) {
        DebugAssistent.doNullCheck(ctx);

        for (ValidationRuleListener l : getListeners()) {
            l.consumeEvent(e, ctx);
        }
    }

    /*
     * Anzahl der Ereignisse
     */
    @Override
    public int getEventCount() {
        return getEvents().size();
    }

    public ValidationLevel getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = ValidationLevel.valueOf(level);
    }

    public void setLevel(ValidationLevel level) {
        this.level = level;
    }

    public String getMessage() {
        if (message == null) {
            setDefaultMessage();
        }
        return message;
    }

    public void setDefaultMessage() {
        setMessage("A " + level.name() + " occured ");
    }

    public void setMessage(String message) {
        DebugAssistent.doNullCheck(message);

        this.message = message;
    }

    public String getName() {
        if (name == null) {
            return this.getClass().getSimpleName();
        }
        return name;
    }

    public void setName(String name) {
        DebugAssistent.doNullCheck(name);

        this.name = name;
    }

    @Override
    public void perform(DataContext context) {
        DebugAssistent.doNullCheck(context);

        boolean ok = isOk(context);
        if (!ok) {
            fireEvents(context);
        }

    }

    @Override
    public boolean isOk(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        if (validator == null) {
            return false;
        }
        return validator.isOk(ctx);
    }

    protected long toLong(Object ob) {
        try {
            if (ob instanceof String) {
                return Long.parseLong((String) ob);
            }
            if (ob instanceof Number) {
                return ((Number) ob).longValue();
            }
            if (ob instanceof Date) {
                ((Date) ob).getTime();
            }
        } catch (Exception ex) {
            LOG.error("Error in toLong", ex);
        }
        return 0;
    }

}