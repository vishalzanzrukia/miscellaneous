package com.db.lock.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class MyConnectionPool {

	private static MyConnectionPool datasource;
	private BasicDataSource ds;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ice_dunnes";
	static final String USER = "root";
	static final String PASS = "P@ssw0rd@123";

	private MyConnectionPool(){
		try {
			ds = new BasicDataSource();
			ds.setDriverClassName(JDBC_DRIVER);
			ds.setUsername(USER);
			ds.setPassword(PASS);
			ds.setUrl(DB_URL);

			// the settings below are optional -- dbcp can work with defaults
			ds.setMinIdle(5);
			ds.setMaxIdle(20);
			ds.setMaxActive(2000);
			ds.setMaxOpenPreparedStatements(2000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MyConnectionPool getInstance(){
		if (datasource == null) {
			datasource = new MyConnectionPool();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection(){
		try {
			return this.ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
