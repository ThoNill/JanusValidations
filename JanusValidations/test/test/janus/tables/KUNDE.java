package test.janus.tables;

import org.janus.fluentSql.Field;
import org.janus.fluentSql.Table;

public class KUNDE extends Table {

	public KUNDE() {
		super();
	}

	public Field name() {
		return new Field(this, "name");
	}
}
