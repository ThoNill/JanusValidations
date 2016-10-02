package org.janus.xml;

import org.apache.commons.beanutils.WrapDynaBean;
import org.janus.helper.DebugAssistent;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Versorgt ein Objekt mit Attributwerten aus einem XML Tag
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */
public class TransferAttributesElement extends Element {

    private static final long serialVersionUID = 3500730358690823720L;

    public TransferAttributesElement() {
        super();
    }

    public void setAttributValues(Object obj) {
        DebugAssistent.doNullCheck(obj);

        setAttributValues(this, obj);
    }

    private static void setAttributValues(Element elem, Object obj) {
        WrapDynaBean dBean = new WrapDynaBean(obj);
        for (Object o : elem.getAttributes()) {
            Attribute attribut = (Attribute) o;
            if (!"class".equals(attribut.getName())) {
                dBean.set(attribut.getName(), attribut.getValue());
            }
        }
    }

}
