package org.janus.standardrules;

/**
 * 
 * Logische INVERTIERUNG der Kinder
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class NotRule extends LogicalRule {

	@Override
	public boolean isOk(int count, int countEvents) {
		return (count > 0);
	}

}
