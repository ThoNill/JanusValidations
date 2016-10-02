package org.janus.standardrules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.helper.DebugAssistent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author THOMAS NILL Lizenz GPLv3
 * 
 *         Verbindung zu Javascript / Rhino f�hrt ein Javascript-Skript aus,
 *         wenn getValue ausgef�hrt wird.
 * 
 */
public class RhinoConnector {

    private static final long serialVersionUID = 6368823261748883157L;

    private String scriptText; // Text des Skriptes

    private static int sNumber = 1; // Z�hler zur Bildung des Skriptnamens

    private String scriptName; // Name des Skriptes

    private Script script = null; // Rhino-Skript

    private List<String> namen = null; // Die Namen der an das Skript zu
                                       // �bergebenden Value Objekte

    private int[] values; // Die Value Objekte

    private Pattern patternDecimalValue = Pattern.compile("^[0-9\\.]+$");

    private static final String reserviert = "        abstract boolean break byte case catch char class const continue default delete do double else export extends false final finally  float for function goto if implements export in instanceof int long native new null package private protected public return short static super switch synchronized this throw throws transient true try typeof var void while with ";

    public RhinoConnector() {
    }

    public void setScript(String scriptText) {
        DebugAssistent.doNullCheck(scriptText);

        this.scriptText = scriptText;
        namen = new ArrayList<String>();

        // Bildung des Skriptnamens
        scriptName = "Script" + incScriptNumber();

        // Aufteilun des Textes
        // Sonderzeichen, die nicht zur Bildung von Variablennamen dienen,
        // trennen
        // in namen stehen also Variablennamen

        // Konstante Strings beachten
        // var i = 'a' ..

        String[] texte = scriptText.split("[\\'\\\"]+");
        for (int n = 0; n <= texte.length / 2; n++) {
            String[] snamen = texte[2 * n].split("[^a-zA-Z0-9\\.\\_]+");
            for (int i = 0; i < snamen.length; i++) {
                // Zahlun und reservierte Woerter ignorieren
                if (!(patternDecimalValue.matcher(snamen[i]).matches() || reserviert
                        .indexOf(" " + snamen[i] + " ") >= 0)) {
                    int p = snamen[i].indexOf('.');
                    if (p > 0) {
                        addName(snamen[i].substring(0, p - 1));
                    } else {
                        addName(snamen[i]);
                    }
                }
            }
        }
    }

    static protected synchronized int incScriptNumber() {
        sNumber++;
        return sNumber;
    }

    private void addName(String n) {
        if (!namen.contains(n)) {
            namen.add(n);
        }
    }

    /**
     * Holt �ber die Namen Variablen aus dem Container und verbindet Sie mit
     * einem Listener
     * 
     * @param model
     */

    public void configure(DataDescription model) {
        DebugAssistent.doNullCheck(model);
        values = new int[namen.size()];
        for (int i = 0; i < namen.size(); i++) {
            values[i] = model.getHandle(namen.get(i));
        }
    }

    /**
     * Kompiliert ein Skript wenn dies n�tig ist
     * 
     * @param cx
     * @return Script
     */
    public Script getScript(Context cx) {
        DebugAssistent.doNullCheck(cx);
        if (script == null) {
            script = cx.compileString(scriptText, scriptName, 0, null);
        }
        return script;
    }

    /**
     * Berechnet den R�ckgabewert eins Skriptes
     * 
     */
    public Serializable getObject(DataContext ctx) {
        DebugAssistent.doNullCheck(ctx);

        Serializable obj = null;

        Context cx = ContextFactory.getGlobal().enterContext();
        try {
            Script s = getScript(cx); // eventuell kompilieren
            if (s != null) {
                Scriptable scope = cx.initStandardObjects();

                // Variablen in den scope Umgebung �bergeben
                for (int i = 0; i < values.length; i++) {
                    Object jsOut = Context.javaToJS(ctx.getObject(values[i]),
                            scope);
                    ScriptableObject.putProperty(scope, namen.get(i), jsOut);
                }
                // und ausf�hren
                obj = (Serializable) script.exec(cx, scope);
            }
        } finally {
            Context.exit();
        }
        return obj;
    }

    public String getScriptText() {
        return scriptText;
    }

}
