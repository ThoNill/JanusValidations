package org.janus.standardrules;

/**
 * 
 * Logische ODER Verknüpfung der Kinder
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class OrRule extends LogicalRule {

	@Override
	public boolean isOk(int count, int countEvents) {
		return (count < countEvents);
	}

}
