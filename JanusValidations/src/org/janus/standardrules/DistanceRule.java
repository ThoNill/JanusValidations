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

package org.janus.standardrules;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;

/**
 * 
 * Prüft ob der Abstand zweier Werte a, b zwischen minDistance und MaxDistance
 * liegt.
 * 
 * @author THOMAS NILL
 * 
 */
public class DistanceRule extends TwoFieldRule {

	private int distMin;
	private int distMax;

	public void setMaxDistance(String maxDistance) {
		DebugAssistent.doNullCheck(maxDistance);

		distMax = Integer.parseInt(maxDistance);
	}

	public void setMinDistance(String minDistance) {
		DebugAssistent.doNullCheck(minDistance);

		distMin = Integer.parseInt(minDistance);
	}

	@Override
	public void configure(DataDescription model) {
		super.configure(model);
	}

	@Override
	protected boolean isOk(Object oa, Object ob, DataContext ctx) {
		long la = toLong(oa);
		long lb = toLong(ob);

		return (distMin <= lb - la && lb - la <= distMax);
	}

}
