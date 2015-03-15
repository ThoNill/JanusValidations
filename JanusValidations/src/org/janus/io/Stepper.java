package org.janus.io;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.janus.rules.EventExtension;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationLevel;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleListener;
import org.janus.rules.ValidationRuleListenerList;
import org.janus.rules.ValidationRuleType;

/**
 * Ein Stepper dient als Filter für ValidationRuleEvents. Die Eingabefelder
 * werden aufgrund einer im Feld fields definierten Reihenfolge sortiert, Das
 * ValidationRuleEvent mit der höchsten Priorität wir ausgewählt und
 * weitergereicht. In einer GUI dient ein Stepper dazu, die in fields
 * vorgegebene Tabulatorreihenfolge einzuhalten.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 * @see ValidationRuleEvent
 * 
 */

public class Stepper implements ValidationRuleListener,
		ValidationRuleListenerList {

	private static final long serialVersionUID = -4151746949497349112L;

	static Logger log = Logger.getLogger("Stepper");

	private Vector<ValidationRuleListener> listeners = new Vector<ValidationRuleListener>();

	/**
	 * Index für die PriorityQueue in einem DataContext
	 */
	private int myQueue = -1;

	/**
	 * Index für ein Array von StepperHelper-Objekten in einem DataContext
	 */
	private int myMapping = -1;

	/**
	 * Durch Leerzeichen separierter String der die Tabulatorreihenfolge der
	 * Felder enthält.
	 */
	private String tabOrder;

	/**
	 * Constructor declaration
	 * 
	 */
	public Stepper() {
		super();
	}

	@SuppressWarnings("unchecked")
	private PriorityQueue<ValidationRuleEvent> getQueue(DataContext ctx) {
		PriorityQueue<ValidationRuleEvent> queue = (PriorityQueue<ValidationRuleEvent>) ctx
				.getObject(myQueue);
		if (queue == null) {
			queue = new PriorityQueue<ValidationRuleEvent>(100, getMapping(ctx));
			ctx.setObject(myQueue, queue);
		}
		return queue;
	}

	private StepperExtension getMapping(DataContext ctx) {
		StepperExtension mapping = (StepperExtension) ctx.getObject(myMapping);
		if (mapping == null) {
			mapping = new StepperExtension(
					(RuleDescription) ctx.getDataDescription(), tabOrder);
			ctx.setObject(myMapping, mapping);
		}
		return mapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.janus.io.RuleListenerList#addRuleListener(org.janus.rules.RuleListener
	 * )
	 */
	@Override
	public void addRuleListener(ValidationRuleListener l) {
		DebugAssistent.doNullCheck(l);

		listeners.addElement(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.janus.io.RuleListenerList#removeRuleListener(org.janus.rules.RuleListener
	 * )
	 */
	@Override
	public void removeRuleListener(ValidationRuleListener l) {
		DebugAssistent.doNullCheck(l);

		listeners.removeElement(l);
	}

	public void fireRuleEvent(ValidationRuleEvent ev, DataContext ctx) {
		DebugAssistent.doNullCheck(ctx, ev);

		Iterator<ValidationRuleListener> i = listeners.iterator();
		while (i.hasNext()) {
			i.next().consumeEvent(ev, ctx);
		}
	}

	public void init(DataContext ctx) {
		DebugAssistent.doNullCheck(ctx);

		getMapping(ctx).clearMapping();
	}

	/**
	 * Method declaration
	 * 
	 * 
	 * @param e
	 * 
	 */
	@Override
	public void consumeEvent(ValidationRuleEvent e, DataContext ctx) {
		if (e == null) {
			throw new NullPointerException();
		}
		PriorityQueue<ValidationRuleEvent> queue = getQueue(ctx);
		StepperExtension mapping = getMapping(ctx);

		if (ValidationRuleType.INIT.equals(e.getType())) {
			// Initialisierung einer Prüfung, neuer Datensatz wird bearbeitet
			// Löschen der Markierungen, wie oft ein Feld besucht wurde.
			mapping.clearMapping();
			queue.clear();
			fireRuleEvent(e, ctx);
		} else if (ValidationRuleType.START.equals(e.getType())) {
			// Start einer Prüfung
			// Entferne die gespeicherten ValidationRuleEvents.
			queue.clear();
			fireRuleEvent(e, ctx);
		} else if (ValidationRuleType.STOP.equals(e.getType())) {
			// Beenden einer Prüfung
			// ausgewähltes Event feuern
			ValidationRuleEvent ev = queue.peek();
			if (ev != null) {
				mapping.incMapping(ev);
				fireRuleEvent(ev, ctx);
			}
			;
			fireRuleEvent(e, ctx);
		} else if (ValidationRuleType.RULE.equals(e.getType())) {
			// Während einer Prüfung wird ein ValidationRuleEvent empfangen

			// Holt sich über die Position (wo ist ein Fehler aufgetreten?) die
			// Information
			// über die Tabulatorposition und wie oft schon Fehler von dieser
			// Position aus
			// gefeuert wurden.
			StepperHelper h = mapping.getData(e);

			if (h == null) {
				throw new RuntimeException("field " + e.getPosition()
						+ " has no Mapping in the Stepper");
			}

			if (h.getCount() > 0
					&& e.getLevel().ordinal() <= ValidationLevel.FOCUS
							.ordinal()) {
				// Bei einer NOTIZ: wenn schon einmal gefeuert ignorieren
				return;
			}

			if (h.getCount() > 1
					&& e.getLevel().ordinal() <= ValidationLevel.WARNING
							.ordinal()) {
				// Warnungen nach dem zweiten Mal ignorieren
				return;
			}

			// ValidationRuleEvent in der Fehlerqueue merken
			if (!queue.contains(e)) {
				queue.add(e);
			}
		}
	}

	public void setTabOrder(String fields) {
		this.tabOrder = fields;
	}

	@Override
	public void configure(DataDescription model) {
		myQueue = model.createAnonymousHandle();
		myMapping = model.createAnonymousHandle();
		Iterator<ValidationRuleListener> i = listeners.iterator();
		// Kinder konfigurieren
		while (i.hasNext()) {
			i.next().configure(model);
		}
	}

}

class StepperExtension extends EventExtension<StepperHelper> implements
		Comparator<ValidationRuleEvent>, Serializable {

	private static final long serialVersionUID = -2365214455825650576L;

	Hashtable<String, StepperHelper> mapping;

	public StepperExtension(RuleDescription description, String tabOrder) {
		super();
		mapping = getMapping(tabOrder);
		configure(description);
		mapping = null;
	}

	private Hashtable<String, StepperHelper> getMapping(String tabOrder) {
		Hashtable<String, StepperHelper> mapping = new Hashtable<String, StepperHelper>();
		mapping = new TabIndexComperator();
		String[] s = tabOrder.split(" +");
		for (int i = 0; i < s.length; i++) {
			mapping.put(s[i], new StepperHelper(s[i], i));
		}

		return mapping;
	}

	public void clearMapping() {
		for (int i = 0; i < getEventCount(); i++) {
			StepperHelper m = getData(i);
			if (m != null) {
				m.init();
			}
		}
	}

	public void incMapping(ValidationRuleEvent ev) {
		StepperHelper m = this.getData(ev);
		if (m != null) {
			m.inc();
		} else {
			throw new RuntimeException("Field " + ev.getPosition()
					+ " is missing ");
		}
	}

	@Override
	public int compare(ValidationRuleEvent arg0, ValidationRuleEvent arg1) {
		if (getData(arg0) == null) {
			throw new RuntimeException("Field " + arg0.getPosition()
					+ " is missing ");
		}
		;
		if (getData(arg1) == null) {
			throw new RuntimeException("Field " + arg1.getPosition()
					+ " is missing ");
		}
		return getData(arg0).getTabindex() - getData(arg1).getTabindex();
	}

	@Override
	protected StepperHelper createDataForEvent(ValidationRuleEvent ev) {
		return mapping.get(ev.getPosition());
	}

}

class StepperHelper {
	/*
	 * Anzahl wie oft ein ValidationRuleEvent zu einer Position (Eingabefeld)
	 * gefeuert wurde.
	 */
	int count;
	/*
	 * Welche Position in der Tabulatorreihenfolge hat das Feld
	 */
	int tabindex;

	String name;

	public StepperHelper(String name, int tabindex) {
		this.count = 0;
		this.name = name;
		this.tabindex = tabindex;
	}

	public int getCount() {
		return count;
	}

	public void inc() {
		this.count++;
	}

	public void init() {
		this.count = 0;
	}

	public int getTabindex() {
		return tabindex;
	}

}