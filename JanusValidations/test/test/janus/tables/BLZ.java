package test.janus.tables;

import org.janus.fluentSql.Field;
import org.janus.fluentSql.Table;

public class BLZ extends Table {

	public BLZ() {
		super();
	}

	public Field iban() {
		return new Field(this, "iban");
	}
}
