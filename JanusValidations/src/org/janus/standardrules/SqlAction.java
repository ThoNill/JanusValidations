package org.janus.standardrules;

import org.janus.actions.Action;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.database.DataContextWithConnection;
import org.janus.db.Statement;
import org.janus.helper.DebugAssistent;

/**
 * Diese Aktion setzt Variablen aufgrund einer Abfrage
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class SqlAction implements Action {

	private Statement stmt;

	public SqlAction() {
		stmt = new Statement();
	}

	public void setStmt(String text) {
		DebugAssistent.doNullCheck(text);

		this.stmt.setText(text);
	}

	public void setValues(String values) {
		this.stmt.setValues(values);
	}

	@Override
	public void configure(DataDescription model) {
		DebugAssistent.doNullCheck(model);
		stmt.setModel(model);
	}

	@Override
	public void perform(DataContext ctx) {
		DebugAssistent.doNullCheckAndOfType(DataContextWithConnection.class,
				ctx);
		try {
			stmt.execute((DataContextWithConnection) ctx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}