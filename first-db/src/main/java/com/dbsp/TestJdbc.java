package com.dbsp;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {

	public static void main(String[] args) {
		
			//shit der in config muss
		String host = "localhost";
		//String port = "5050";
		String port = "5432";
		String db_name = "postgres";
		String username = "postgres";
		String password = "1234";
		
		String jdbcURL = "jdbc:postgresql://"+host+":"+port+"/"+db_name+"";
		
		Connection conn = null;
		
		try {
			System.out.println("Connecting to: " + jdbcURL);
			conn = DriverManager.getConnection(jdbcURL, username, password);
			
			if(conn!=null) {
				System.out.println("Connection OK!");
			} else {
				System.out.println("Connection FAILED!");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
