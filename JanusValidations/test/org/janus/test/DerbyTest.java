package org.janus.test;

import java.sql.Connection;


import org.junit.Test;
import static org.junit.Assert.*;

import test.janus.db.ConnectionSource;

public class DerbyTest {

	public DerbyTest() {

	}

	@Test
	public void dbVerbindung() {
		try {
			Connection con = ConnectionSource.getDataSource().getConnection();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
