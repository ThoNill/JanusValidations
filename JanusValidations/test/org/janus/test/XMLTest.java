package org.janus.test;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.log4j.PropertyConfigurator;
import org.janus.io.CSVReader;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleListenerList;
import org.janus.rules.ValidationRuleMaschine;
import org.janus.xml.ValidationRuleElementFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class XMLTest {

	@BeforeClass
	public static void init() throws Exception {
		PropertyConfigurator.configure(XMLTest.class.getClassLoader()
				.getResource("config/log4j.properties"));
	}

	@Test
	public void testLaden() {
		try {
			RuleDescription model = new RuleDescription();
			ValidationRuleListenerList rules = ValidationRuleElementFactory
					.createRuleList(model, "./test/org/janus/test/",
							"rules1.xml");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error " + e.getMessage());
		}
	}

	@Test
	public void testRead() {
		try {
			RuleDescription model = new RuleDescription();
			ValidationRuleMaschine rules = ValidationRuleElementFactory
					.createRuleList(model, "./test/org/janus/test/",
							"rules1.xml");
			CSVReader reader = new CSVReader();
			reader.setDescription(model);
			reader.setRules(rules);

			BufferedReader b = new BufferedReader(new FileReader(
					"./test/org/janus/test/test.txt"));

			reader.read(b);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error " + e.getMessage());
		}
	}

	@Test
	public void testRead2() {
		try {
			RuleDescription model = new RuleDescription();
			ValidationRuleMaschine rules = ValidationRuleElementFactory
					.createRuleList(model, "./test/org/janus/test/",
							"rules2.xml");

			CSVReader reader = new CSVReader();
			// reader.setMessageSource("outMsg1");
			reader.setDescription(model);
			reader.setRules(rules);

			BufferedReader b = new BufferedReader(new FileReader(
					"./test/org/janus/test/test.txt"));

			reader.read(b);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error " + e.getMessage());
		}
	}

}
