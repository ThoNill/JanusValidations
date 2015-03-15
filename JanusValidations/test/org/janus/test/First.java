package org.janus.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.janus.io.CSVReader;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleEventLogger;
import org.janus.rules.ValidationRuleMaschine;
import org.janus.standardrules.LengthRule;
import org.janus.standardrules.RegExpRule;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class First {

	@BeforeClass
	public static void init() throws Exception {
		PropertyConfigurator.configure(First.class.getClassLoader()
				.getResource("config/log4j.properties"));
	}

	@Test
	public void test1() throws IOException {

		RuleDescription model = new RuleDescription();
		ValidationRuleMaschine rules = new ValidationRuleMaschine(model);

		LengthRule lr = new LengthRule();
		lr.setMin("5");
		lr.setMax("7");
		lr.setField("name");
		rules.addRuleOrAction(lr);

		RegExpRule rr = new RegExpRule();
		rr.setPattern("^[0-9]*$");
		rr.setField("name");
		rules.addRuleOrAction(rr);

		ValidationRuleEventLogger logger = new ValidationRuleEventLogger();
		rules.addRuleListener(logger);
		rules.configure();

		CSVReader reader = new CSVReader();
		reader.setDescription(model);
		reader.setRules(rules);

		BufferedReader b = new BufferedReader(new FileReader(
				"./test/org/janus/test/test.txt"));

		reader.read(b);
	}
}
