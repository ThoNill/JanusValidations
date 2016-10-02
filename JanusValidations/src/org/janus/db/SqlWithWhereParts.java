package org.janus.db;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.data.Message;
import org.janus.data.MessageImpl;
import org.janus.helper.DebugAssistent;

/**
 * Texte für Abfragen können von der Form
 * 
 * select a,b, from tabelle where a = $(nr) { and b = $(p) } order by a
 * 
 * sein. Texte in {,} werden nur bedingt eingesetzt, wenn die Werte $(nr) nicht
 * leer sind
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class SqlWithWhereParts implements Message {

    MessageImpl whereParts[] = null;

    public void setText(String text) {
        DebugAssistent.doNullCheck(text);

        String[] t = MessageImpl.splitText(text, "{", "}");
        whereParts = new MessageImpl[t.length];
        for (int i = 0; i < t.length; i++) {
            whereParts[i] = new MessageImpl(t[i]);
        }
    }

    public void setModel(DataDescription model) {
        for (int i = 0; i < whereParts.length; i++) {
            whereParts[i].initHandles(model);
        }

    }

    @Override
    public String getMessage(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < whereParts.length; i++) {
            MessageImpl message = whereParts[i];
            String text = message.getMessage(ctx);
            if (whereParts.length > 1 && i % 2 == 0) {
                if (!allValuesEmpty(ctx, message.getHandles())) {
                    buffer.append(text);
                }
            } else {
                buffer.append(text);
            }
        }
        return buffer.toString();
    }

    /**
     * Prüft ob die Werte der Schlüssel in einem DataContext alle "Leer" sind
     * null,"","0" gelten dabei als Leer.
     * 
     * @param ctx
     * @return
     */
    public boolean allValuesEmpty(DataContext ctx, int handles[]) {
        DebugAssistent.doNullCheck(ctx);

        if (handles == null) {
            return true;
        }
        int anz = handles.length;
        for (int i = 0; i < anz; i++) {
            Object obj = ctx.getObject(handles[i]);
            obj = convertToString(obj);
            if (!isEmpty(obj)) {
                return false;
            }
        }
        return true;
    }

    private Object convertToString(Object obj) {
        if (obj != null) {
            return obj.toString().trim();
        }
        return null;
    }

    private boolean isEmpty(Object obj) {
        return obj == null || "".equals(obj) || "0".equals(obj);
    }

}
