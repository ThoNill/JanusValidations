package org.janus.io;

import java.io.BufferedReader;
import java.io.IOException;

import org.janus.data.DataContextImpl;
import org.janus.data.DataDescriptionImpl;
import org.janus.helper.DebugAssistent;
import org.janus.rules.RuleDescription;
import org.janus.rules.ValidationRuleListenerList;
import org.janus.rules.ValidationRuleMaschine;

/**
 * CVSReader liest Daten aus einer CVSDatei und gibt Sie an die Prüfung weiter.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class CSVReader {
	/**
	 * Benutzter Reader
	 */
	private BufferedReader reader;

	/**
	 * Liste der Prüfungen, die zu erfüllen sind
	 */
	private ValidationRuleMaschine rules;

	/**
	 * Model der Daten, Beschreibung der Felder
	 */
	private RuleDescription description;

	/**
	 * Indexe der Datenfelder des DataModel
	 */
	private int[] indexe = null;

	/**
	 * Liest aus dem Reader eine Reihe Datenwerte und prüft sie.
	 * 
	 * @param reader
	 * @throws IOException
	 */
	public void read(BufferedReader reader) throws IOException {
		DebugAssistent.doNullCheck(reader);

		String first = reader.readLine();
		if (first != null) {
			String[] header = first.split(" *\\| *");
			indexe = getHandles(header);
			rules.configure(description);

			DataContextImpl ctx = description.newContext();
			rules.init(ctx);
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(" *\\| *");
				ctx.setObjects(indexe, values);
				rules.perform(ctx);
			}
		}
	}

	private synchronized int[] getHandles(String[] names) {
		DebugAssistent.doNullCheck(names);

		int count = names.length;
		int[] erg = new int[count];
		for (int i = 0; i < count; i++) {
			erg[i] = description.getHandle(names[i]);
		}
		return erg;
	}

	public DataDescriptionImpl getDescription() {
		return description;
	}

	public void setDescription(RuleDescription model) {
		DebugAssistent.doNullCheck(model);

		this.description = model;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		DebugAssistent.doNullCheck(reader);

		this.reader = reader;
	}

	public ValidationRuleListenerList getRules() {
		return rules;
	}

	public void setRules(ValidationRuleMaschine rules) {
		DebugAssistent.doNullCheck(rules);

		this.rules = rules;
	}

}
