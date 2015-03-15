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

/**
 * 
 * Vergleich zwei Werte a, b miteinander
 * 
 * @author THOMAS NILL
 *
 * @see CompareModus
 */
public class CompareRule extends TwoFieldRule {
	protected CompareModus modus;

	public void setModus(String smodus) {
		this.modus = CompareModus.valueOf(smodus);
	}

	@Override
	protected boolean isOk(Object oa, Object ob, DataContext ctx) {
		if (CompareModus.EQ == modus) {
			return oa == ob || (oa != null && oa.equals(ob));
		}
		if (CompareModus.NEQ == modus) {
			return !(oa == ob || (oa != null && oa.equals(ob)));
		}
		if (CompareModus.LT == modus) {
			return toLong(oa) < toLong(ob);
		}
		if (CompareModus.GT == modus) {
			return toLong(oa) > toLong(ob);
		}
		if (CompareModus.GET == modus) {
			return toLong(oa) >= toLong(ob);
		}
		if (CompareModus.LET == modus) {
			return toLong(oa) <= toLong(ob);
		}

		return false;
	}
}
