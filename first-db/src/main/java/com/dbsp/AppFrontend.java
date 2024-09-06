package com.dbsp;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.dbsp.entity.*;
import java.util.List;

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
        System.out.println("3. getProducts");
        System.out.println("4. getProductsByCategoryPath");

        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {

                    case 0: {
                        // hier wird finish methode aufgerufen
                        dbService.finish();
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;
                    }
                    case 1: {
                        System.out.print("Enter shop name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter shop street: ");
                        String street = scanner.nextLine();
                        System.out.print("Enter shop zip code: ");
                        int zip = scanner.nextInt();

                        dbService.addShop(name, street, zip);
                        System.out.println("Shop added successfully.");
                        break;
                    }
                    case 2: {
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
                    }
                    case 3: {
                        System.out.print("Enter title pattern:");
                        String pattern = scanner.nextLine();
                        List<Item> products = dbService.getProducts(pattern);
                        if (products != null && !products.isEmpty()) {
                            System.out.println("Products matching the pattern:");
                            for (Item p : products) {
                                System.out.println("ASIN: " + p.getAsin() + ", Title: " + p.getTitle());
                            }
                        } else {
                            System.out.println("No products found matching the pattern: " + pattern);
                        }
                    }

                    case 4: {
                        dbService.printAllCategoryTitles();
                        System.out.print("Enter Category Path like this: Category1 > Category2 > Category 3");
                        String categoryPath = scanner.nextLine();

                        List<Item> products = dbService.getProductsByCategoryPath(categoryPath);

                        if (products != null && !products.isEmpty()) {
                            System.out.println("Products in category path:");
                            for (Item product : products) {
                                System.out.println("ASIN: " + product.getAsin() + ", Title: " + product.getTitle());
                            }
                        } else {
                            System.out.println("No products found in the given category path: " + categoryPath);
                        }
                    }

                    /*
                     * default:
                     * System.out.println("Invalid choice. Please try again.");
                     * break;
                     */
                }

            } else {
                // Handle non-integer input
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
}
