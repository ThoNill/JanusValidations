package org.janus.test;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.janus.fluentSql.SqlCreator.create;
import static org.janus.fluentSql.SqlCreator.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.janus.data.DataDescriptionImpl;
import org.janus.database.DataContextWithConnection;
import org.junit.Before;
import org.junit.Test;

import test.janus.db.ConnectionSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

public class BuildDbTest {

	public BuildDbTest() {
	}

	private DataDescriptionImpl description = new DataDescriptionImpl();
	private DataContextWithConnection dataSource = new DataContextWithConnection(
			description);

	@Before
	public void prepare() throws Exception {
		dataSource.setConnectionSource(ConnectionSource.getDataSource());
		try (Connection con = dataSource.getConnection()) {

			Object sql = create("VENDOR").field("ID").t_int().field("NAME")
					.t_char(20);
			Statement stmt = con.createStatement();
			stmt.execute(sql.toString());
		} catch (SQLException ex) {
			if (ex.getErrorCode() != 30000) {
				ex.printStackTrace();
			}
		}

		Operation operation = sequenceOf(deleteAllFrom("VENDOR"),
				insertInto("VENDOR").columns("ID", "NAME").values(1, "Amazon")
						.values(2L, "PriceMinister").build());
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource),
				operation);
		dbSetup.launch();
	}

	@Test
	public void dbVerbindung() {
		Object sql = select().column("NAME").from("VENDOR").where().field("ID")
				.eq().integer(1);
		Connection con;
		try {
			con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(sql.toString());
			if (result.next()) {
				assertEquals("Amazon", result.getString(1).trim());
			}
			result.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			fail(e.getLocalizedMessage());
		}
	}

}
