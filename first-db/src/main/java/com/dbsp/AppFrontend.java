package com.dbsp;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.dbsp.entity.*;

public class AppFrontend {

    public static void main(String[] args) {
        // Lade die parameter aus der Property-Datei
        /*
         * Properties dbProperties = new Properties();
         * try (FileInputStream input = new
         * FileInputStream("src/main/resources/db.properties")) {
         * dbProperties.load(input);
         * } catch (IOException e) {
         * e.printStackTrace();
         * System.out.println("Properties konnten nicht geladen werden.");
         * return;
         * }
         */

        Scanner scanner = new Scanner(System.in);
        // Erstelle den Service und initialisiere ihn
        DBService dbService = new DBService();
        dbService.init(); // Initialisierung des Services mit Properties

        System.out.println("Shop Management System");
        System.out.println("0. Exit");
        System.out.println("1. Add a new shop");
        System.out.println("2. GetProduct");

        while (true) {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {

                case 0:
                    // hier wird finish methode aufgerufen
                    dbService.finish();
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;

                case 1:
                    System.out.print("Enter shop name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter shop street: ");
                    String street = scanner.nextLine();
                    System.out.print("Enter shop zip code: ");
                    int zip = scanner.nextInt();

                    dbService.addShop(name, street, zip);
                    System.out.println("Shop added successfully.");
                    break;

                case 2:
                    System.out.print("Enter asin: ");
                    String asin = scanner.nextLine();
                    Item product = dbService.getProduct(asin);

                    if (product != null) {
                        System.out.println("Product Details:");
                        System.out.println("Title: " + product.getTitle());
                        System.out.println("Group: " + product.getPgroup());
                        System.out.println("Sales Rank: " + product.getSalesrank());
                        // Weitere Produktinformationen anzeigen...
                    } else {
                        System.out.println("No product found with ASIN: " + asin);
                    }

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
