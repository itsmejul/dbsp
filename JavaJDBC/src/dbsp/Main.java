package dbsp;

import java.sql.Connection;

public class Main {
	public static void main(String[] agrs) {
		
		//shit der in config muss
		String host = "localhost";
		//String port = "5050";
		String port = "5432";
		String db_name = "postgres";
		String username = "postgres";
		String password = "1234";
		
		ConnectDB db = new ConnectDB();
		//init - Verbindung herstellen
		Connection conn = db.connect_to_db(host, port, db_name, username, password);
		
		//finish -Verbindung trennen
		//conn = null; //so?
		
		//getProduct - für ProductId alle Details bekommen
		
		//getProducts(String pattern) - Liste an Producten deren Titel mit übergebenen Pattern übereinstimmt
		//Hinweis: SQL-Operator Like
		
		//getCategoryTree - kompletten KategorieBaum durch Rückgabe Wurzelknoten
		
		//getProductsByCategoryPath - Angabe einer Kategorie (definiert durch Pfad von Wurzel zu sich selbst) Rückgabe Liste der zugeordneten Produkte
		
		//getTopProducts - Liste aller Produkte unter den Top "k" basierend auf Rating
		
		//getSimilarCheaperProduct - Eingabe Produkt(Id) Ausgabe Liste von Produkten ähnlich und billiger
		
		//addNewReview - Rahmenapplikation erlaubt Ansehen und Hinzufügen von Reviews
		
		//getTrolls - Liste von Nutzern ausgeben, deren Durchschnittsbewertung unter einem spezifizierten Rating ist
		
		//getOffers - für Produkt ID alle verfügbaren Angebote
	}
}
