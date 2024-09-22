package com.dbsp;

import java.util.List;
import java.util.Scanner;

import com.dbsp.entity.Customer;
import com.dbsp.entity.Item;
import com.dbsp.entity.Price;
import com.dbsp.entity.ProductReviews;
import com.dbsp.extra.Category;
import static com.dbsp.extra.Colors.COLOR_CYAN;
import static com.dbsp.extra.Colors.COLOR_CYAN_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_GREEN;
import static com.dbsp.extra.Colors.COLOR_GREEN_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_RED;
import static com.dbsp.extra.Colors.COLOR_RESET;
import static com.dbsp.extra.Colors.COLOR_WHITE;

import com.dbsp.extra.InputCheck;

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

        while (true) {
            System.out.println(COLOR_GREEN_BACKGROUND + " Shop Management System " + COLOR_RESET);
            System.out.println(COLOR_CYAN + "0" + COLOR_RESET + ". Exit");
            System.out.println(COLOR_CYAN + "1" + COLOR_RESET + ". GetProduct");
            System.out.println(COLOR_CYAN + "2" + COLOR_RESET + ". getProducts");
            System.out.println(COLOR_CYAN + "3" + COLOR_RESET + ". getProductsByCategoryPath");
            System.out.println(COLOR_CYAN + "4" + COLOR_RESET + ". getTopProducts");
            System.out.println(COLOR_CYAN + "5" + COLOR_RESET + ". getSimilarCheaperProduct");
            System.out.println(COLOR_CYAN + "6" + COLOR_RESET + ". addNewReview");
            System.out.println(COLOR_CYAN + "7" + COLOR_RESET + ". showReviews");
            System.out.println(COLOR_CYAN + "8" + COLOR_RESET + ". getTrolls");
            System.out.println(COLOR_CYAN + "9" + COLOR_RESET + ". getOffers");
            System.out.println(COLOR_CYAN + "10" + COLOR_RESET + ". GetCategoryTree");
            System.out.println(COLOR_CYAN + "11" + COLOR_RESET + ". Add a new shop");
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {

                    // void finish();
                    case 0: {
                        dbService.finish();
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;
                    }

                    // Item getProduct(String asin);
                    case 1: {
                        String asin = null;
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print("Enter asin: ");
                            asin = scanner.nextLine();
                        }
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
                        break;
                    }

                    // List<Item> getProducts(String pattern);
                    case 2: {
                        String pattern = null;
                        while (!InputCheck.patternCheck(pattern)) {
                            System.out.print("Enter title pattern:");
                            pattern = scanner.nextLine();
                        }
                        List<Item> products = dbService.getProducts(pattern);
                        if (products != null && !products.isEmpty()) {
                            System.out.println("Products matching the pattern:");
                            for (Item p : products) {
                                System.out.println("ASIN: " + p.getAsin() + ", Title: " + p.getTitle());
                            }
                        } else {
                            System.out.println("No products found matching the pattern: " + pattern);
                        }
                        break;
                    }

                    // List<Item> getProductsByCategoryPath(String categoryPath);
                    case 3: {

                        // User-Eingabe lesen und speichern
                        System.out.println(COLOR_CYAN_BACKGROUND + COLOR_WHITE
                                +
                                "Please enter the category path, seperated by the '>'-Symbol. (e.g., 'Formate>Box-Sets>Dance &amp; Electronic'):"
                                + COLOR_RESET);
                        String categoryPath = scanner.nextLine();

                        // Aufruf der entsprechenden dbService-Methode
                        List<Item> items = dbService.getProductsByCategoryPath(categoryPath);

                        // dbService gibt eine Liste an Items zurück.
                        // Hier werden alle Items mit asin und Titel in die Konsole ausgegeben
                        // Da theoretisch jede Eingabe ein Kategorienname sein könnte, wird hier der
                        // Input nicht weiter überprüft. Bei ungültigen Eingaben wird also einfach eine
                        // leere Liste an items returnt
                        if (items.isEmpty()) {
                            System.out.println(COLOR_CYAN_BACKGROUND + COLOR_WHITE
                                    + "No items found for the given category path." + COLOR_RESET);
                        } else {
                            // Gib die gefundenen Items mit asin und titel aus
                            System.out.println(COLOR_CYAN_BACKGROUND + COLOR_WHITE + "Items found in category '"
                                    + categoryPath + "':"
                                    + COLOR_RESET);
                            for (Item item : items) {
                                System.out.println(COLOR_CYAN + "asin: " + COLOR_RESET + item.getAsin() + COLOR_CYAN
                                        + " Titel: " + COLOR_RESET + item.getTitle());
                            }
                        }

                        break;
                    }

                    // List<Item> getTopProducts(int k);
                    case 4: {
                        System.out.println("Enter amount of top products:");
                        if (scanner.hasNextInt()) {
                            int k = scanner.nextInt();
                            // nextInt eigtl (nicht) safe
                            //
                            List<Item> topProducts = dbService.getTopProducts(k);
                            if (topProducts != null && !topProducts.isEmpty()) {
                                System.out.println("Top " + k + " products:");
                                for (Item product : topProducts) {
                                    System.out.println("ASIN: " + product.getAsin() + ", Title: " + product.getTitle());
                                }
                            } else {
                                System.out.println("No top products found.");
                            }
                        } else {
                            // Handle non-integer input
                            System.out.println(COLOR_RED + "Invalid input. Please enter a valid number." + COLOR_RESET);
                            scanner.nextLine(); // Clear the invalid input
                        }
                        break;
                    }

                    // List<Item> getSimilarCheaperProduct(String asin);
                    case 5: {
                        String asin = null;
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print("Enter asin: ");
                            asin = scanner.nextLine();
                        }
                        List<Item> similarProducts = dbService.getSimilarCheaperProduct(asin);
                        if (similarProducts != null && !similarProducts.isEmpty()) {
                            System.out.println("Similar products:");
                            for (Item product : similarProducts) {
                                System.out.println("ASIN: " + product.getAsin() + ", Title: " + product.getTitle());
                            }
                        } else {
                            System.out.println("No similar products found.");
                        }
                        break;
                    }

                    // void addNewReview(String asin, int rating, int helpful, String reviewDate,
                    // int customerId, String summary, String content);
                    case 6: {
                        // check alles
                        String asin = null;
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.println("Enter asin: ");
                            asin = scanner.nextLine();
                        }
                        Integer rating = null;
                        while (!InputCheck.ratingCheck(rating)) {
                            System.out.println("Enter rating:");
                            if (scanner.hasNextInt()) {
                                rating = scanner.nextInt();
                                scanner.nextLine();
                            } else {
                                System.out.println(
                                        COLOR_RED + "Invalid input. Please enter a valid number." + COLOR_RESET);
                            }
                        }
                        Integer helpful = null;
                        System.out.println("Enter helpful (leave empty for null):");
                        String helpful_input = scanner.nextLine(); // Nimm die Eingabe als String
                        // Überprüfen, ob die Eingabe leer ist (d.h. der Benutzer möchte "null")
                        if (helpful_input.isEmpty()) {
                            helpful = null; // helpful bleibt null
                        } else {
                            try {
                                // Versuche, die Eingabe in eine Zahl umzuwandeln
                                helpful = Integer.parseInt(helpful_input);
                            } catch (NumberFormatException e) {
                                // Ungültige Eingabe (keine Zahl)
                                System.out.println(COLOR_RED
                                        + "Invalid input. Please enter a valid number or leave it empty for null."
                                        + COLOR_RESET);
                            }
                        }

                        String reviewDate = java.time.LocalDate.now().toString();

                        Integer customerId = null;
                        System.out.println("Enter customerId:");
                        String customerid_input = scanner.nextLine();
                        if (customerid_input.isEmpty()) {
                            customerId = null;
                        } else {
                            try {
                                customerId = Integer.parseInt(customerid_input);
                            } catch (NumberFormatException e) {
                                System.out.println(COLOR_RED
                                        + "Invalid input. Please enter a valid number or leave it empty for null."
                                        + COLOR_RESET);
                            }
                        }
                        String summary = null;
                        while (!InputCheck.summaryCheck(summary)) {
                            System.out.println("Enter summary:");
                            summary = scanner.nextLine();
                        }
                        String content = null;
                        while (!InputCheck.summaryCheck(content)) {
                            System.out.println("Enter content:");
                            content = scanner.nextLine();
                        }

                        dbService.addNewReview(asin, rating, helpful, reviewDate, customerId, summary, content);
                        System.out.println("Review added successfully.");
                        break;
                    }

                    // List<ProductReviews> showReviews(String asin);
                    case 7: {
                        String asin = null;
                        // check asin
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print("Enter asin: ");
                            asin = scanner.nextLine();
                        }
                        // dann weiter mit der asin
                        List<ProductReviews> reviews = dbService.showReviews(asin);
                        if (reviews != null && !reviews.isEmpty()) {
                            System.out.println("Reviews for " + asin + ":");
                            for (ProductReviews review : reviews) {
                                System.out.println(
                                        COLOR_CYAN + "-----------------------------------------------" + COLOR_RESET);
                                System.out.println("Rating: " + review.getRating());
                                System.out.println("Helpful: " + review.getHelpful());
                                System.out.println("Review Date: " + review.getReviewDate());
                                System.out.println("Customer ID: " + review.getCustomerId());
                                System.out.println("Summary: " + review.getSummary());
                                System.out.println("Content: " + review.getContent());
                            }
                        } else {
                            System.out.println("No reviews found for " + asin);
                        }
                        break;
                    }

                    // List<Customer>getTrolls(double averageRating);
                    case 8: {
                        System.out.println("Enter average rating:");
                        double averageRating = scanner.nextDouble();
                        scanner.nextLine();
                        // check between 1 & 5
                        List<Customer> trolls = dbService.getTrolls(averageRating);
                        if (trolls != null && !trolls.isEmpty()) {
                            System.out.println("Trolls with average rating of " + averageRating + ":");
                            for (Customer troll : trolls) {
                                System.out.println("Customer ID: " + troll.getId());
                                System.out.println("Name: " + troll.getUsername());
                                System.out.println("Iban: " + troll.getIban());
                                System.out.println("-----------------------------------------------");
                            }
                        } else {
                            System.out.println("No trolls found with average rating of " + averageRating);
                        }
                        break;
                    }

                    // List<Price>getOffers(String asin);
                    case 9: {
                        String asin = null;
                        // check asin
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print("Enter asin: ");
                            asin = scanner.nextLine();
                        }
                        // dann weiter mit der asin
                        List<Price> offers = dbService.getOffers(asin);
                        if (offers != null && !offers.isEmpty()) {
                            System.out.println("Offers for " + asin + ":");
                            for (Price offer : offers) {
                                System.out.println("Price State: " + offer.getPrice_state() + " Price Value: "
                                        + offer.getPrice_value() + " Price Mult: " + offer.getPrice_mult()
                                        + " Price Currency: " + offer.getPrice_currency());
                                System.out.println("at Shop: " + offer.getPrice_shop_id());
                                System.out.println("-----------------------------------------------");
                            }
                        } else {
                            System.out.println("No offers found for " + asin);
                        }
                        break;
                    }
                    case 10: {

                        // Erstelle den Kategorie-Baum mit der Methode aus DBService
                        Category topCategory = dbService.getCategoryTree();

                        // Da es mehrere Top-Level Kategorien gibt, wird eine Künstliche "TopCategory"
                        // als Wurzel erzeugt
                        // Aufruf der rekursiven printCategory-methode
                        // Nutzt Ausgabe im Stil von "tree" command (directories ausgeben)
                        printCategory(topCategory, "", true);

                        break;
                    }

                    // extra ...
                    case 11: {
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

                    default:
                        System.out.println(
                                COLOR_RED + "This choice doesn't exist (yet). Please try again." + COLOR_RESET);
                        break;

                }

            } else {
                // Handle non-integer input
                System.out.println(COLOR_RED + "Invalid input. Please enter a valid number." + COLOR_RESET);
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    // Rekursive Hilfsmethode, um den Category-Baum auszugeben
    private static void printCategory(Category category, String prefix, boolean isLast) {
        // Aktuellen Kategorienamen ausgeben
        // vor dem Namen wird ein Prefix ausgegeben, worin bspw leerzeichen und Linien
        // der Baumstruktur sind, damit alles akkurat aussieht
        // Linien vor dem Namen sind abhängig davon, ob es das letzte Element auf dieser
        // Ebene ist
        System.out.println(prefix + (isLast ? COLOR_GREEN + "└── " + COLOR_RESET : COLOR_GREEN + "├── " + COLOR_RESET)
                + category.getName());

        // Subkategorien der aktuellen Kategorie in Liste sammeln
        List<Category> subCategories = category.getSubCategories();

        // Iteriere durch die Subkategorien und rufe printCategory für sie alle rekursiv
        // auf
        for (int i = 0; i < subCategories.size(); i++) {
            // Damit die "Baumstrukturlinien" beim letzten Kindelement abschliessen, wird
            // hier geprüft, welche Subcategory die letzte ist
            boolean lastSubCategory = (i == subCategories.size() - 1);
            // Rekursiver Aufruf
            // Damit die Kinder immer nach rechts verschoben angezeigt werden, wird hier das
            // prefix um ein Paar Leerzeichen erweitert
            printCategory(subCategories.get(i), prefix + (isLast ? "    " : COLOR_GREEN + "│   " + COLOR_RESET),
                    lastSubCategory);
        }

    }
}
