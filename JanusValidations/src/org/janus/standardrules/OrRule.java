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
		System.out.println(this.getClass().getName() + " Count=" + count
				+ " gegenüber " + countEvents);
		return (count < countEvents);
	}

}
