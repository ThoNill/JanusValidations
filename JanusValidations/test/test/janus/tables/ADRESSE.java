package test.janus.tables;

import org.janus.fluentSql.Field;
import org.janus.fluentSql.Table;

public class ADRESSE extends Table {

	public ADRESSE() {
		super();
	}

	public Field strasse() {
		return new Field(this, "strasse");
	}

	public Field plz() {
		return new Field(this, "plz");
	}

	public Field ort() {
		return new Field(this, "ort");
	}

}
