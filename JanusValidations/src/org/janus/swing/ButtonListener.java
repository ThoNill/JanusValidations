package org.janus.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.janus.data.DataContext;
import org.janus.helper.DebugAssistent;
import org.janus.rules.ValidationRuleMaschine;

/**
 * Fängt das Auslösen eines JButtonObjektes ab. Wir von Analyse mit JButtons
 * verbunden.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 * @see Analyse
 */
public class ButtonListener implements ActionListener {
    private JButton button;

    private DataContext context;

    private ActionListener[] listeners;

    private ValidationRuleMaschine rules;

    public void setButton(JButton button) {
        DebugAssistent.doNullCheck(button);

        if (this.button != null) {
            this.button.removeActionListener(this);
            if (listeners != null) {
                for (int i = 0; i < listeners.length; i++) {
                    this.button.addActionListener(listeners[i]);
                }
            }
        }
        ;
        this.button = button;

        listeners = button.getActionListeners();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                button.removeActionListener(listeners[i]);
            }
        }
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DebugAssistent.doNullCheck(ev);

        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].actionPerformed(ev);
            }
        }
        rules.init(context);
        rules.perform(context);
    }

    public void setContext(DataContext context) {
        DebugAssistent.doNullCheck(context);

        this.context = context;
    }

    public void setRules(ValidationRuleMaschine rules) {
        DebugAssistent.doNullCheck(rules);

        this.rules = rules;
    }

}
