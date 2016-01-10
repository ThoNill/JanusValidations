package test.janus.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

public class ConnectionSource {
	private static DataSource dataSource = null;

	public static void init() throws Exception {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

		GenericObjectPool connectionPool = new GenericObjectPool(null);
		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				"jdbc:derby:mybase1;create=true", null);
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);
		dataSource = new PoolingDataSource(connectionPool);

	}

	public static DataSource getDataSource() throws Exception {
		if (dataSource == null) {
			init();
		}
		return dataSource;
	}

}
