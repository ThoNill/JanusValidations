package org.janus.rules;

import org.janus.helper.DebugAssistent;

/**
 * Fehler, Warnung, Fokus auf eine Feld. Diese Events werden in der
 * Fehlerprüfung weitergegeben.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class ValidationRuleEvent {

    private int index;

    private ValidationRuleType type;

    private ValidationLevel level;

    private String name;

    private String message;

    private String position;

    public ValidationRuleEvent(ValidationRuleType type, ValidationLevel level,
            String name, String position, String message) {
        DebugAssistent.doNullCheck(type, level);

        this.type = type;
        this.level = level;
        this.name = name;
        this.position = position;
        this.message = message;

    }

    public ValidationLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getPosition() {
        return position;
    }

    public ValidationRuleType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValidationRuleEvent other = (ValidationRuleEvent) obj;
        if (index != other.index)
            return false;
        if (level != other.level)
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ValidationRuleEvent [index=" + index + ", type=" + type
                + ", level=" + level + ", name=" + name + ", message="
                + message + ", position=" + position + "]";
    }

}
