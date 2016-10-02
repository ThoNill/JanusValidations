package org.janus.rules;

/**
 * Mehrere Listener addieren und entfernen
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public interface ValidationRuleListenerList {

    void addRuleListener(ValidationRuleListener l);

    void removeRuleListener(ValidationRuleListener l);

}