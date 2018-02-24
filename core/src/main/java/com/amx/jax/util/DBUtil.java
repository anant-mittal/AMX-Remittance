package com.amx.jax.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

	public static void closeResources(CallableStatement cs, Connection connection) {
		if (cs != null) {
			try {
				cs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
