package org.janus.rules;

import org.janus.data.Configurable;
import org.janus.data.DataContext;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public interface Validator extends Configurable {
    boolean isOk(DataContext ctx);
}
