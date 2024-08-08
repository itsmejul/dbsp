package dbsp;

public class Main {
	public static void main(String[] agrs) {
		
		//shit der in config muss
		String host = "localhost";
		String port = "5050";
		String db_name = "postgres";
		String username = "postgres";
		String password = "1234";
		
		ConnectDB db = new ConnectDB();
		db.connect_to_db(host, port, db_name, username, password);
	}
}
