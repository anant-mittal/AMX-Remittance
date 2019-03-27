package com.amx.jax.db.multitenant;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantContextHolder;

@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DataSource dataSource;

	@Autowired
	TenantDBConfig tenantDBConfig;

	@Override
	public Connection getAnyConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifie) throws SQLException {
		DataSource ds = tenantDBConfig.getDataSource();
		Connection connection;

		if (ds != null) {
			connection = ds.getConnection();
		} else {
			connection = getAnyConnection();
		}
		return connection;
	}

	public DataSource getDataSource() {
		String tenantIdentifier = TenantContextHolder.currentSite().toString();
		DataSource ds = tenantDBConfig.getDataSource();
		return ds;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		connection.close();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return null;
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}
}
