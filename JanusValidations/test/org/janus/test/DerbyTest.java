package org.janus.test;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.junit.Test;

import test.janus.db.ConnectionSource;

public class DerbyTest {
    private static final Logger LOG = Logger.getLogger(DerbyTest.class);

    public DerbyTest() {

    }

    @Test
    public void dbVerbindung() {
        try {
            Connection con = ConnectionSource.getDataSource().getConnection();
            con.close();
        } catch (Exception e) {
            LOG.error("Fehler", e);
            ;
            fail(e.getLocalizedMessage());
        }
    }

}
