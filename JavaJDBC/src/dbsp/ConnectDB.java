package dbsp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectDB {
	public Connection connect_to_db(String host,String port,String db_name,String username,String password){
		
		Connection conn = null;

		try {
			
			Class.forName("org.postgresql.Driver"); //Driver muss in config!
			conn = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db_name+"",""+username+"",""+password+""); //Server muss auch in config!
			// mein Port ist 5050 PostgreSQL ist aber default auf 5432! können wir/ich auch noch ändern!
			
			if(conn!=null) {
				System.out.println("Connection OK!");
			} else {
				System.out.println("Connection FAILED!");
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return conn;
	}
	
	//brauchen wir für reviews ...
	public void createTable(Connection conn, String table_name) {
		Statement statement;
		try {
			String query="create table "+table_name+"(testID SERIAL, testName varchar(200), primary key(testID));";
			statement = conn.createStatement();
			statement.executeUpdate(query);
			System.out.println("Table Created");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public void insert_row(Connection conn, String table_name, String testName) {
		Statement statement;
		try {
			String query=String.format("insert into %s(testName) values('%s');",table_name, testName);
			statement = conn.createStatement();
			statement.executeUpdate(query);
			System.out.println("Row inserted");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
