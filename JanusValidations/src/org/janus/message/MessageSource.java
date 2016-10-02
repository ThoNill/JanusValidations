package org.janus.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.janus.data.DefaultClassFactory;
import org.janus.helper.DebugAssistent;

/**
 * 
 * Manchmal werden unterschiedliche Ausgaben für eine Fehlermeldung benötigt.
 * Abhängig von der Sprache oder der Ausgabeart. Eine MessageSource stellt diese
 * Texte bereit. Die Texte müssen als text/*.properties gespeichert sein.
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class MessageSource {
    private static final Logger LOG = Logger.getLogger(MessageSource.class);
    /**
     * Globale Map mit den Daten der Property-Dateien
     */
    private static HashMap<String, Properties> map = new HashMap<String, Properties>();

    /**
     * Holt den in einer text/*.properties Datei enthaltenen Text.
     * 
     * @param messageFileName
     * @param msgKey
     * @return
     */
    public static String getText(String messageFileName, String msgKey) {
        DebugAssistent.doNullCheck(messageFileName, msgKey);

        if (map == null) {
            map = new HashMap<String, Properties>();
        }
        Properties p = map.get(messageFileName);
        if (p == null) {
            // wenn in der globalen Map noch nicht vorhanden, Texte laden.
            p = new Properties();
            try {
                p.load(DefaultClassFactory.FACTORY.getResource("text/"
                        + messageFileName + ".properties"));
                map.put(messageFileName, p);
            } catch (IOException e) {
                LOG.error("Fehler", e);
                ;
                return msgKey;
            }
        }
        String smsg = p.getProperty(msgKey);
        return (smsg == null) ? msgKey : smsg;
    }
}
