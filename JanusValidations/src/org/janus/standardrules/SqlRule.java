package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.database.DataContextWithConnection;
import org.janus.db.Statement;
import org.janus.helper.DebugAssistent;

/**
 * Diese Regel prüft die Existenz aufgrund einer Abfrage
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class SqlRule extends MultiFieldRule {

	private Statement stmt;

	public SqlRule() {
		super();
		stmt = new Statement();
	}

	public void setStmt(String text) {
		DebugAssistent.doNullCheck(text);

		this.stmt.setText(text);
	}

	@Override
	public void setFields(String text) {

		super.setFields(text);
		this.stmt.setValues(text);
	}

	@Override
	public void configure(DataDescription model) {
		super.configure(model);
		stmt.setModel(model);
	}

	@Override
	public boolean isOk(DataContext ctx) {
		DebugAssistent.doNullCheckAndOfType(DataContextWithConnection.class,
				ctx);

		try {
			Object obj = stmt.queryObject((DataContextWithConnection) ctx);
			if (obj instanceof Number) {
				Number n = (Number) obj;
				if (n.intValue() > 0) {
					return true;
				}
			} else {
				if (obj != null && !("".equals(obj.toString().trim()))) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return false;
	}
}
