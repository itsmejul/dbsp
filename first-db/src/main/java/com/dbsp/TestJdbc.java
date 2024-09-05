package com.dbsp;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {

	public static void main(String[] args) {

		// shit der in config muss
		String host = "localhost";
		// String port = "5050";
		String port = "5432";
		String db_name = "dbsp";
		String username = "postgres";
		String password = "postgres";
		// musste maven.compiler.source und target von 1.7 zu 17 ändern
		// und musste password von 1234 zu postgres ändern auch in hibernate config
		String jdbcURL = "jdbc:postgresql://" + host + ":" + port + "/" + db_name + "";

		Connection conn = null;

		try {
			System.out.println("Connecting to: " + jdbcURL);
			conn = DriverManager.getConnection(jdbcURL, username, password);

			if (conn != null) {
				System.out.println("Connection OK!");
			} else {
				System.out.println("Connection FAILED!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
