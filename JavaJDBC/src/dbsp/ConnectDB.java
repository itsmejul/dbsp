package dbsp;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
	public Connection connect_to_db(String host,String port,String db_name,String username,String password){
		
		Connection connection = null;

		try {
			
			Class.forName("org.postgresql.Driver"); //Driver muss in config!
			connection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db_name+"",""+username+"",""+password+""); //Server muss auch in config!
			// mein Port ist 5050 PostgreSQL ist aber default auf 5432! können wir/ich auch noch ändern!
			
			if(connection!=null) {
				System.out.println("Connection OK!");
			} else {
				System.out.println("Connection FAILED!");
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return connection;
	}
}
