package org.janus.standardrules;

import java.util.regex.Pattern;

import org.janus.data.DataContext;
import org.janus.helper.DebugAssistent;

/**
 * Prüft auf Übereinstimmung mit einem regulären Audruck
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class RegExpRule extends OneFieldRule {
    private Pattern pattern;

    public RegExpRule() {
        super(null);
    }

    @Override
    public boolean isOk(DataContext ctx) {
        Object o = getField(ctx);
        if (o instanceof String) {
            return pattern.matcher((String) o).matches();
        }
        return false;
    }

    @Override
    public void setDefaultMessage() {
        setMessage("The field " + getField() + " = $(" + getField()
                + ") don't fullfill " + getPattern());
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public void setPattern(String pattern) {
        DebugAssistent.doNullCheck(pattern);

        this.pattern = Pattern.compile(pattern);
    }

}
