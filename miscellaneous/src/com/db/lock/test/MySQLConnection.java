package com.db.lock.test;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MySQLConnection {

	public static void updateStock() {
		PreparedStatement preparedStatement = null;
		Connection connection = MyConnectionPool.getInstance().getConnection();
		try {
//			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			// System.out.println("TransactionIsolation : " + connection.getTransactionIsolation());
			// update stock_level set no_in_stock = no_in_stock + 1, last_updated_dts='2015-01-19 00:08:23', last_updated_by='SYSTEM' where stock_location_id = 1 and sku = '82988';
			preparedStatement = connection.prepareStatement("update stock_level set no_in_stock = no_in_stock - ?, last_updated_dts=?, last_updated_by=? where stock_location_id = ? and sku = ? ");
			preparedStatement.setInt(1, 1);
			preparedStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, "vzanzrukia");
			preparedStatement.setInt(4, 1);
			preparedStatement.setString(5, "82988");
			preparedStatement.executeUpdate();
			Thread.sleep(5000);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
