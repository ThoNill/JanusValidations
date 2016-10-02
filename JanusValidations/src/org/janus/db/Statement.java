package org.janus.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.janus.data.DataContext;
import org.janus.data.DataDescription;
import org.janus.database.DataContextWithConnection;
import org.janus.helper.DebugAssistent;

/**
 * Abfragekomponente des Validation-Frameworks
 * 
 * @author THOMAS NILL Lizenz GPLv3
 */
public class Statement {
    /**
     * Namen der Variablen, in die das Ergebnis der Abfrage eingelesen wird
     */
    String[] values;

    /**
     * Die schlüsselwerte auf diese Variablen im DataContext
     * 
     * @see DataContext
     */
    int keys[] = null;

    /**
     * Der Text, der die Abfrage generiert
     */
    SqlWithWhereParts text;

    /**
     * Abfrage d.h. executeQuery oder query
     */
    boolean isQuery = true;

    /**
     * soll nur die erste Zeile des Abfrageergebnisses berücksichtigt werden
     */
    boolean firstRow = true;

    /**
     * Sollen die Ergebnisse kurz gecacht werden.
     */
    boolean withCache = true;

    /**
     * Schlüssel auf den Cachewert im DataContext
     */
    int cache;

    /**
     * Konstruktor
     * 
     * @param text
     *            Abfrage
     * @param values
     *            Feldnamen, in die das Ergenis geschieben wird
     */
    public Statement(String text, String values) {
        super();
        setText(text);
        setValues(values);
    }

    public Statement() {
        super();
    }

    public void setText(String text) {
        DebugAssistent.doNullCheck(text);

        this.text = new SqlWithWhereParts();
        this.text.setText(text);
    };

    public void setValues(String valuesAsString) {
        this.values = null;
        if (valuesAsString != null) {
            this.values = valuesAsString.split(" +");
        }
    }

    /**
     * Verknüpfung mit dem DataModel
     * 
     * @param model
     * @see DataDescription
     * 
     */
    public void setModel(DataDescription model) {
        DebugAssistent.doNullCheck(model);

        if (values != null) {
            keys = new int[values.length];
            for (int i = 0; i < keys.length; i++) {
                keys[i] = model.getHandle(values[i]);
            }
        }
        text.setModel(model);
        if (withCache) {
            cache = model.createAnonymousHandle();
        }
    }

    /**
     * Ausführen der Abfrage
     * 
     * @param ctx
     *            Ablaufkontext
     * @throws Exception
     */
    public void execute(DataContextWithConnection ctx) throws SQLException {
        DebugAssistent.doNullCheck(ctx);

        String sStmt = text.getMessage(ctx);

        if (withCache) {
            StatementCache c = (StatementCache) ctx.getObject(cache);
            // Falls es einen Cahce gibt und sich an der Abfrage nichts geändert
            // hat
            // die Werte des Cahe verwenden
            if (c != null && sStmt.equals(c.sStmt)) {
                for (int i = 0; i < keys.length; i++) {
                    ctx.setObject(keys[i], c.values[i]);
                }
                return;
            }
        }

        Connection con;

        con = ctx.getConnection();
        if (con != null) {
            java.sql.Statement stmt = con.createStatement();
            if (isQuery) {
                ResultSet result = stmt.executeQuery(sStmt);
                if (firstRow) {

                    if (result.next()) {
                        for (int i = 0; i < keys.length; i++) {
                            ctx.setObject(keys[i],
                                    (Serializable) result.getObject(i + 1));
                        }
                    } else {
                        for (int i = 0; i < keys.length; i++) {
                            ctx.setObject(keys[i], "");
                        }
                    }
                    if (withCache) {
                        // den bisherigen Cahce wiederverwenden
                        StatementCache c = (StatementCache) ctx
                                .getObject(cache);
                        // oder Eventuell neu erzeugen;
                        if (c == null) {
                            c = new StatementCache();
                            c.values = new Serializable[keys.length];
                        }
                        // den Cache mit Werten versorgen
                        c.sStmt = sStmt;
                        for (int i = 0; i < keys.length; i++) {
                            c.values[i] = ctx.getObject(keys[i]);
                        }
                        // und speichern;
                        ctx.setObject(cache, c);
                    }
                    ;
                } else {
                    int columns = result.getMetaData().getColumnCount();
                    ArrayList<Serializable> daten = new ArrayList<Serializable>();
                    while (result.next()) {
                        Serializable[] d = new Serializable[columns];
                        for (int i = 0; i < keys.length; i++) {
                            d[i] = (Serializable) result.getObject(i + 1);
                        }
                        daten.add(d);
                    }
                    ctx.setObject(keys[0], daten);
                }
                result.close();
            } else {
                stmt.execute(sStmt);
            }
            stmt.close();
        }
    }

    public Object queryObject(DataContextWithConnection ctx)
            throws SQLException {
        DebugAssistent.doNullCheck(ctx);

        String sStmt = text.getMessage(ctx);

        if (withCache) {
            StatementCache c = (StatementCache) ctx.getObject(cache);
            // Falls es einen Cahce gibt und sich an der Abfrage nichts geändert
            // hat
            // die Werte des Cahe verwenden
            if (c != null && sStmt.equals(c.sStmt)) {
                return c.values[0];
            }
        }

        Connection con;
        con = ctx.getConnection();

        Serializable res = null;
        if (con != null) {
            java.sql.Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(sStmt);
            if (result.next()) {
                for (int i = 0; i < keys.length; i++) {
                    res = (Serializable) result.getObject(1);
                }
            }
            result.close();
            stmt.close();
        }

        if (withCache) {
            // den bisherigen Cahce wiederverwenden
            StatementCache c = (StatementCache) ctx.getObject(cache);
            // oder Eventuell neu erzeugen;
            if (c == null) {
                c = new StatementCache();
                c.values = new Serializable[1];
            }
            // den Cache mit Werten versorgen
            c.sStmt = sStmt;
            c.values[0] = res;
            // und speichern;
            ctx.setObject(cache, c);
        }
        ;

        return res;
    }
}

class StatementCache implements Serializable {
    public String sStmt;
    public Serializable[] values;
}