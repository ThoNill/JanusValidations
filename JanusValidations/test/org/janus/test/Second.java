package org.janus.test;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.janus.data.DataContextImpl;
import org.janus.io.Stepper;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleEventLogger;
import org.janus.rules.ValidationRuleMaschine;
import org.janus.standardrules.FocusRequestRule;
import org.janus.standardrules.LengthRule;
import org.janus.standardrules.RegExpRule;
import org.janus.swing.Analyse;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class Second {
    private static final Logger LOG = Logger.getLogger(Second.class);

    @BeforeClass
    public static void init() {
        PropertyConfigurator.configure(Second.class.getClassLoader()
                .getResource("config/log4j.properties"));
    }

    @Test
    public void test1() throws IOException {

        RuleDescription model = new RuleDescription();
        ValidationRuleMaschine rules = new ValidationRuleMaschine(model);

        LengthRule lr = new LengthRule();
        lr.setMin("5");
        lr.setMax("7");
        lr.setField("vorname");
        rules.addRuleOrAction(lr);

        RegExpRule rr = new RegExpRule();
        rr.setPattern("^[0-9]*$");
        rr.setField("name");
        rules.addRuleOrAction(rr);

        RegExpRule r2 = new RegExpRule();
        r2.setPattern("^[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}$");
        r2.setField("datum");
        rules.addRuleOrAction(r2);

        FocusRequestRule fr = new FocusRequestRule();
        fr.setField("button");
        rules.addRuleOrAction(fr);

        FocusRequestRule fr1 = new FocusRequestRule();
        fr1.setField("name");
        rules.addRuleOrAction(fr1);

        FocusRequestRule fr2 = new FocusRequestRule();
        fr2.setField("vorname");
        rules.addRuleOrAction(fr2);

        FocusRequestRule fr3 = new FocusRequestRule();
        fr3.setField("datum");
        rules.addRuleOrAction(fr3);

        ValidationRuleEventLogger logger = new ValidationRuleEventLogger();
        logger.configure(model);
        rules.addRuleListener(logger);

        Stepper stepper = new Stepper();
        stepper.setTabOrder("name vorname datum button");
        rules.addRuleListener(stepper);

        rules.configure(model);

        Analyse a = new Analyse(logger.getMessageExtension());
        stepper.addRuleListener(a);
        a.setRules(rules);
        a.setContext(new DataContextImpl(model));

        a.configure(model);

        JFrame f = generateFrame();
        a.analyse(f, model);
        f.setVisible(true);
    }

    public JFrame generateFrame() {
        JTextField f1 = new JTextField();
        f1.setName("name");
        f1.setBorder(new TitledBorder("name"));
        JTextField f2 = new JTextField();
        f2.setName("vorname");
        f2.setBorder(new TitledBorder("vorname"));
        JTextField f3 = new JTextField();
        f3.setBorder(new TitledBorder("datum"));
        f3.setName("datum");
        JButton button = new JButton("Test");
        button.setName("button");

        JPanel panel = new JPanel(new GridLayout(4, 0));

        panel.add(f1);
        panel.add(f2);
        panel.add(f3);
        panel.add(button);
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 60, 180);
        frame.getContentPane().add(panel);

        return frame;
    }

    public static void main(String args[]) {
        try {
            init();
            Second s = new Second();
            s.test1();
        } catch (Exception ex) {
            LOG.error("Fehler", ex);
            ;
        }
    }
}
