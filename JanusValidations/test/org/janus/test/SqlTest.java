/* Legal Stuff
 *
 * JANUS_VALIDATION is Open Source.
 *
 * Copyright (c) 2009 Thomas Nill.  All rights reserved.
 * E-Mail t.nill@t-online.de
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted under the terms of the 
 * GNU LESSER GENERAL PUBLIC LICENSE version 2.1 or later.
 */

package org.janus.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.PropertyConfigurator;
import org.janus.data.DataDescriptionImpl;
import org.janus.database.DataContextWithConnection;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleMaschine;
import org.janus.xml.ValidationRuleElementFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.janus.db.ConnectionSource;

public class SqlTest {
	private DataSource dataSource;

	public SqlTest() {
	}

	@Before
	public void init() throws Exception {

		PropertyConfigurator.configure(First.class.getClassLoader()
				.getResource("config/log4j.properties"));

		dataSource = ConnectionSource.getDataSource();

		File f = new File("./mybase1");
		boolean createTables = !f.exists();
		if (createTables) {
			doStatement(" CREATE Table namen ( name char(10) )");
			doStatement(" CREATE Table namen1 ( name char(10) )");
			doStatement(" insert into namen ( name) values ('thomas' ) ");
		}

	}

	public void doStatement(String s) throws Exception {
		Connection con = getConnection();
		Statement stmt = con.createStatement();
		stmt.execute(s);
		stmt.close();
		con.commit();
		con.close();
	}

	protected Connection getConnection() throws Exception {
		// return
		// DriverManager.getConnection("jdbc:apache:commons:dbcp:testpool2");
		return dataSource.getConnection();
	}

	@Test
	public void test1() throws Exception {
		DataDescriptionImpl model = new DataDescriptionImpl();

		org.janus.db.Statement stmt = new org.janus.db.Statement();
		stmt.setText("select count(*) from namen ");
		stmt.setValues("anzahl");
		stmt.setModel(model);

		DataContextWithConnection ctx = new DataContextWithConnection(model);
		ctx.setConnectionSource(dataSource);

		Object o1 = stmt.queryObject(ctx);

		if (!(o1 instanceof Number)) {
			fail("Object [count] is not a Number");
		} else {
			assertTrue(((Number) o1).intValue() == 1);
		}
		Object o2 = stmt.queryObject(ctx);// Test des Cache
		assertEquals(o1, o2);
	}

	@Test
	public void test2() throws Exception {
		DataDescriptionImpl model = new DataDescriptionImpl();

		int kc = model.getHandle("c");
		int kname = model.getHandle("name");

		org.janus.db.Statement stmt = new org.janus.db.Statement();
		stmt.setValues("c name");
		stmt.setText("select 1 , name from namen ");
		stmt.setModel(model);

		DataContextWithConnection ctx = new DataContextWithConnection(model);
		ctx.setConnectionSource(dataSource);

		stmt.execute(ctx);

		Number oc = (Number) ctx.getObject(kc);
		String oname = (String) ctx.getObject(kname);
		assertEquals(oc, new Integer(1));
		assertEquals(oname.trim(), "thomas");

		ctx.setObject(kc, "bla");
		ctx.setObject(kname, "bla");

		stmt.execute(ctx); // Test des Cache

		oc = (Number) ctx.getObject(kc);
		oname = (String) ctx.getObject(kname);
		assertEquals(oc, new Integer(1));
		assertEquals(oname.trim(), "thomas");

	}

	@Test
	public void test3() throws Exception {
		RuleDescription model = new RuleDescription();
		ValidationRuleMaschine rules = ValidationRuleElementFactory
				.createRuleList(model, "./test/org/janus/test/", "sql1.xml");
		rules.configure(model);

		DataContextWithConnection ctx = new DataContextWithConnection(model);
		ctx.setConnectionSource(dataSource);

		int count = model.getHandle("count");

		rules.init(ctx);
		rules.perform(ctx);

		Object c = ctx.getObject(count);
		if (!(c instanceof Number)) {
			fail("Object [count] is not a Number");
		} else {
			assertTrue(((Number) c).intValue() == 1);
		}

	}

	@After
	public void deinit() throws Exception {
	}

}
