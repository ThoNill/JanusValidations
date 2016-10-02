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
import org.janus.data.Message;

/**
 * 
 * Prüft ob ein Wert x zwischen min und max liegt
 * 
 * @author THOMAS NILL
 *
 */
public class BetweenRule extends MultiFieldRule {
    private String max;
    private String min;
    private String x;

    private int vmax;
    private int vmin;
    private Message vx;

    public void setMax(String max) {
        this.max = max;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Override
    public void configure(DataDescription model) {
        super.configure(model);
        vmax = Integer.parseInt(max);
        vmin = Integer.parseInt(min);
        vx = createMessage(model, x);
    }

    @Override
    public boolean isOk(DataContext ctx) {
        long lx = toLong(vx.getMessage(ctx));
        return (vmin <= lx && lx <= vmax);
    }

}
