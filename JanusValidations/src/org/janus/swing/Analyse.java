package org.janus.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import org.janus.data.Configurable;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.janus.rules.MessageExtension;
import org.janus.rules.ValidationLevel;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleListener;
import org.janus.rules.ValidationRuleListenerList;
import org.janus.rules.ValidationRuleMaschine;
import org.janus.rules.ValidationRuleType;

/**
 * 
 * Eine Klasse mit drei verschiedenen Funktionen - Analysiert ein GUI Frame,
 * bzw. Dialog verbindet sich dabei mit Eingabefeldern - Übergibt Werte aus den
 * Eingabefeldern an die Prüfung - positioniert aufgrund des Prüfergebnisses auf
 * der GUI
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class Analyse implements ActionListener, ValidationRuleListener,
		Configurable {
	private ArrayList<ComponentKey> components;

	private HashMap<String, Component> hComponents;

	private HashMap<String, Integer> hCount;

	private DataContext context = null;

	private ValidationRuleMaschine rules;

	private MessageExtension messageExtension;

	public Analyse(MessageExtension messageExtension) {
		this.messageExtension = messageExtension;
		components = new ArrayList<ComponentKey>();
		hComponents = new HashMap<String, Component>();
		hCount = new HashMap<String, Integer>();
	}

	/**
	 * 
	 * Analysieren der Eingabefelder eines Dialoges oder Frames
	 * 
	 * @param comp
	 * @param model
	 */
	public void analyse(Component comp, DataDescription model) {
		DebugAssistent.doNullCheck(comp, model);

		if (comp instanceof JFrame) {
			analyse(((JFrame) comp).getContentPane(), model);
		} else if (comp instanceof JDialog) {
			JDialog dial = (JDialog) comp;
			dial.setModal(true);
			dial.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			analyse(dial.getContentPane(), model);
		} else if (comp instanceof JScrollPane) {
			JScrollPane s = (JScrollPane) comp;
			analyse(s.getViewport().getView(), model);
		} else if (comp instanceof JButton) {
			String name = comp.getName();
			if (name != null) {
				hComponents.put(name, comp);
			}
			JButton button = (JButton) comp;
			ButtonListener lb = new ButtonListener();
			lb.setContext(context);
			lb.setRules(rules);
			lb.setButton(button);
		} else if (comp instanceof JTextComponent || comp instanceof JList
				|| comp instanceof JComboBox || comp instanceof JRadioButton
				|| comp instanceof JCheckBox) {
			String name = comp.getName();
			if (name != null) {
				ComponentKey key = new ComponentKey();
				key.comp = comp;
				key.key = model.getHandle(name);
				components.add(key);
				hComponents.put(name, comp);
				if (comp instanceof JTextField) {
					JTextField t = (JTextField) comp;
					t.addActionListener(this);
				}

			}
			;
		} else if (comp instanceof Container) {
			Component[] cv = ((Container) comp).getComponents();

			for (int i = 0; i < cv.length; i++) {
				Component cc = cv[i];
				analyse(cc, model);
			}

		}
	}

	/**
	 * 
	 * Füllt einen DataContext mit Werten aus Eingabefelden damit Sie der
	 * Prüfung zu übergeben werden können.
	 * 
	 * @param ctx
	 */

	public void fillDataContext(DataContext ctx) {
		DebugAssistent.doNullCheck(ctx);

		for (ComponentKey key : components) {
			String text = "";
			if (key.comp instanceof JTextComponent) {
				text = ((JTextComponent) key.comp).getText();
			} else if (key.comp instanceof JList) {
				JList list = (JList) key.comp;
				int si = list.getMinSelectionIndex();
				text = list.getModel().getElementAt(si).toString();
			} else if (key.comp instanceof JComboBox) {
				JComboBox box = (JComboBox) key.comp;
				text = box.getSelectedItem().toString();
			} else if (key.comp instanceof JCheckBox) {
				JCheckBox box = (JCheckBox) key.comp;
				text = (box.isSelected()) ? "true" : "false";
			} else if (key.comp instanceof JRadioButton) {
				JRadioButton box = (JRadioButton) key.comp;
				text = (box.isSelected()) ? "true" : "false";
			}
			ctx.setObject(key.key, text);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		fillDataContext(context);
		rules.perform(context);
	}

	public DataContext getContext() {
		return context;
	}

	public void setContext(DataContext context) {
		DebugAssistent.doNullCheck(context);

		this.context = context;
	}

	public ValidationRuleListenerList getRules() {
		return rules;
	}

	public void setRules(ValidationRuleMaschine rules) {
		DebugAssistent.doNullCheck(rules);

		this.rules = rules;
	}

	/**
	 * 
	 * Bearbeiten eines ValidationRuleEvents
	 * 
	 */
	@Override
	public void consumeEvent(ValidationRuleEvent ev, DataContext context) {
		DebugAssistent.doNullCheck(ev, context);

		if (ValidationRuleType.INIT.equals(ev.getType())) {
			hCount.clear();
		}
		;

		if (ValidationRuleType.RULE.equals(ev.getType())) {
			String position = ev.getPosition();
			Component comp = hComponents.get(position);
			if (comp != null) {
				Integer c = hCount.get(position);
				if (c == null) {
					hCount.put(position, new Integer(1));
					comp.requestFocus();
				} else {
					hCount.put(position, new Integer(c + 1));
					if (ValidationLevel.WARNING.equals(ev.getLevel())) {
						if (c == 1) {
							JOptionPane.showMessageDialog(null,
									messageExtension.getMessage(ev, context),
									"WARNING", JOptionPane.WARNING_MESSAGE);
							comp.requestFocus();
						}
						;
					} else if (ValidationLevel.ERROR.equals(ev.getLevel())) {
						JOptionPane.showMessageDialog(null,
								messageExtension.getMessage(ev, context),
								"ERROR", JOptionPane.ERROR_MESSAGE);
						comp.requestFocus();
					} else if (ValidationLevel.ERROR.equals(ev.getLevel())) {
						JOptionPane.showMessageDialog(null,
								messageExtension.getMessage(ev, context),
								"FATAL", JOptionPane.ERROR_MESSAGE);
						comp.requestFocus();
					}
				}
			}
		}
	}

	@Override
	public void configure(DataDescription description) {
		DebugAssistent.doNullCheck(description);

		messageExtension.configure(description);
	}
}
