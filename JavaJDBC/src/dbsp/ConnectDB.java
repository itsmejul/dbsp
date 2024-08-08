package dbsp;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
	public static void main(String[] agrs) {
		
		Connection connection = null;
		
		try {
			
			Class.forName("org.postgresql.Driver"); //Driver muss in config!
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5050/postgres","postgres","1234"); //Server muss auch in config!
			// mein Port ist 5050 PostgreSQL ist aber default auf 5432! können wir/ich auch noch ändern!
			// 2. Attribut Username 3. Attribut Passwort! default username postgress ; mein pw ist 1234
			
			if(connection!=null) {
				System.out.println("Connection OK!");
			} else {
				System.out.println("Connection FAILED!");
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
}
