package org.janus.standardrules;

import org.janus.actions.Action;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;

/**
 * Diese Aktion setzt eine Variable auf den Rückgabewert eines
 * Javascriptausdruckes
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class SetValue implements Action {

	private RhinoConnector rhino;

	private int iSet;

	private String set = null;

	public SetValue() {
		rhino = new RhinoConnector();
	}

	public void setSet(String set) {
		this.set = set;
	}

	public void setTo(String scriptText) {
		DebugAssistent.doNullCheck(scriptText);

		rhino.setScript(scriptText);
	}

	@Override
	public void configure(DataDescription model) {
		DebugAssistent.doNullCheck(model);

		rhino.configure(model);
		iSet = model.getHandle(set);
	}

	@Override
	public void perform(DataContext context) {
		DebugAssistent.doNullCheck(context);

		context.setObject(iSet, rhino.getObject(context));
	}
}
