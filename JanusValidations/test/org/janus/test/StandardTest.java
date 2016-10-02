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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleEvent;
import org.janus.rules.ValidationRuleListener;
import org.janus.rules.ValidationRuleMaschine;
import org.janus.rules.ValidationRuleType;
import org.janus.xml.ValidationRuleElementFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class StandardTest implements ValidationRuleListener {

    private static final Logger LOG = Logger.getLogger(StandardTest.class);

    List<String> result = new ArrayList<String>();
    String[] standardvalues = { "vorname", "thomas", "datum", "30.01.2009",
            "b", "5", "x", "5", "d", "6", "fif1", "12345", "ifswitch1", "0",
            "for1", "0123456", "for2", "012345678910", "fand1", "0123456",
            "fand2", "012345678910", "fnot", "0" };

    @BeforeClass
    public static void init() {
        PropertyConfigurator.configure(StandardTest.class.getClassLoader()
                .getResource("config/log4j.properties"));
    }

    @Test
    public void testLaden() {
        try {
            RuleDescription model = new RuleDescription();
            ValidationRuleMaschine rules = ValidationRuleElementFactory
                    .createRuleList(model, "./test/org/janus/test/",
                            "standard.xml");
            rules.configure(model);
        } catch (Exception e) {
            LOG.error("Fehler", e);
            ;
            fail("Error " + e.getMessage());
        }
    }

    @Test
    public void test1() {
        tester(new String[] { "r1" }, new String[] { "vorname", "e" });
    }

    @Test
    public void test2() {
        tester(new String[] { "r2" }, new String[] { "datum", "e" });
    }

    @Test
    public void test3() {
        tester(new String[] { "r1", "r2" }, new String[] { "vorname", "e",
                "datum", "e" });
    }

    @Test
    public void test4() {
        tester(new String[] { "r3" }, new String[] { "b", "2" });
    }

    @Test
    public void test5() {
        tester(new String[] { "r4" }, new String[] { "x", "-2" });
    }

    @Test
    public void test6() {
        tester(new String[] { "r5" }, new String[] { "d", "200" });
    }

    @Test
    public void test7() {
        tester(new String[] { "r5" }, new String[] { "d", "-200" });
    }

    @Test
    public void test8() {
        tester(new String[] { "r2if1" }, new String[] { "ifswitch1", "1" });
    }

    @Test
    public void test9() {
        tester(new String[] {}, new String[] { "for2", "1" });
    }

    @Test
    public void test10() {
        tester(new String[] { "or" }, new String[] { "for1", "1", "for2", "1" });
    }

    @Test
    public void test11() {
        tester(new String[] { "and" }, new String[] { "fand2", "1" });
    }

    @Test
    public void test12() {
        tester(new String[] { "and" }, new String[] { "fand1", "1", "fand2",
                "1" });
    }

    @Test
    public void test13() {
        tester(new String[] { "not" }, new String[] { "fnot", "12345" });
    }

    public void tester(String[] testresult, String[] testvalues) {
        try {
            result = new ArrayList<String>();

            RuleDescription model = new RuleDescription();

            for (int i = 0; i < standardvalues.length; i = i + 2) {
                model.getHandle(standardvalues[i]);
            }

            ValidationRuleMaschine rules = ValidationRuleElementFactory
                    .createRuleList(model, "./test/org/janus/test/",
                            "standard.xml");
            rules.addRuleListener(this);
            rules.configure();

            DataContext ctx = model.newContext();

            for (int i = 0; i < standardvalues.length; i = i + 2) {
                int n = model.getHandle(standardvalues[i]);
                ctx.setObject(n, standardvalues[i + 1]);
            }

            for (int i = 0; i < testvalues.length; i = i + 2) {
                int n = model.getHandle(testvalues[i]);
                ctx.setObject(n, testvalues[i + 1]);
            }

            rules.init(ctx);
            rules.perform(ctx);

            for (int i = 0; i < result.size(); i = i + 2) {
                System.out.println("Result " + result.get(i));
            }

            for (int i = 0; i < testresult.length; i = i + 2) {
                System.out.println("TestResult " + testresult[i]);
            }

            if (result.size() != testresult.length) {
                fail("Number of the results different from the expected Value ");
            }
            for (int i = 0; i < testresult.length; i = i + 2) {
                assertEquals(testresult[i], result.get(i));
            }

        } catch (Exception e) {
            LOG.error("Fehler", e);
            ;
            fail("Error " + e.getMessage());
        }
    }

    @Override
    public void configure(DataDescription model) {

    }

    @Override
    public void consumeEvent(ValidationRuleEvent ev, DataContext context) {
        if (ev.getType() == ValidationRuleType.RULE) {
            String name = ev.getName();
            if (!result.contains(name)) {
                result.add(name);
            }
        }
    }

}
