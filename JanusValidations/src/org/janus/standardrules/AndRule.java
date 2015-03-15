package org.janus.standardrules;

/**
 * 
 * Logische UND Verkn�pfung der Kinder
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class AndRule extends LogicalRule {

	@Override
	public boolean isOk(int count, int countEvents) {
		return count == 0;
	}

}
