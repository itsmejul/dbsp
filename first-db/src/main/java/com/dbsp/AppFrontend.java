package com.dbsp;

import java.util.List;
import java.util.Scanner;

import com.dbsp.entity.Customer;
import com.dbsp.entity.Item;
import com.dbsp.entity.Price;
import com.dbsp.entity.ProductReviews;
import com.dbsp.extra.Category;

import static com.dbsp.extra.Colors.COLOR_BLUE_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_CYAN;
import static com.dbsp.extra.Colors.COLOR_GREEN;

import static com.dbsp.extra.Colors.COLOR_RED_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_GREEN_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_RED;
import static com.dbsp.extra.Colors.COLOR_RESET;
import static com.dbsp.extra.Colors.COLOR_WHITE;
import com.dbsp.extra.InputCheck;

public class AppFrontend {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        // Erstelle den Service und initialisiere ihn
        AppInterface dbService = new DBService();
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
            System.out.print(COLOR_BLUE_BACKGROUND + "Enter your choice: " + COLOR_RESET);

            // Nur int Eingaben erlauben
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {

                    // void finish();
                    case 0: {
                        // Beende System und schließe DB-Verbindung
                        dbService.finish();
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;
                    }

                    // Item getProduct(String asin);
                    case 1: {
                        String asin = null;
                        // Mit scanner so lange abfragen, bis User etwas gültiges eingibt (richtige
                        // Länge etc)
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print(COLOR_BLUE_BACKGROUND + "Enter asin (e.g. '3401024434'): " + COLOR_RESET);
                            asin = scanner.nextLine();
                        }
                        // Aufruf der Methode aus dbService
                        Item product = dbService.getProduct(asin);

                        if (product != null) {
                            // Ausgabe aller Daten zum gefundenen Produkt
                            System.out.println(COLOR_BLUE_BACKGROUND + "Product Details:" + COLOR_RESET);
                            System.out.println(COLOR_CYAN + "ASIN: " + COLOR_RESET + product.getAsin());
                            System.out.println(COLOR_CYAN + "Title: " + COLOR_RESET + product.getTitle());
                            System.out.println(COLOR_CYAN + "Group/Type: " + COLOR_RESET + product.getPgroup());
                            System.out.println(COLOR_CYAN + "Sales Rank: " + COLOR_RESET + product.getSalesrank());
                            System.out.println(COLOR_CYAN + "EAN: " + COLOR_RESET + product.getEan());
                            System.out.println(COLOR_CYAN + "Average Review Score: " + COLOR_RESET
                                    + product.getAvg_review_score());
                            System.out.println(COLOR_CYAN + "Picture Link: " + COLOR_RESET + product.getPicture());
                            System.out
                                    .println(COLOR_CYAN + "Detail page Link: " + COLOR_RESET + product.getDetailpage());

                        } else {
                            // Wenn product null ist, existiert in der DB kein Produkt mit der angegebenen
                            // ASIN
                            System.out.println(
                                    COLOR_BLUE_BACKGROUND + "No product found with ASIN: " + asin + COLOR_RESET);
                        }
                        break;
                    }

                    // List<Item> getProducts(String pattern);
                    case 2: {
                        String pattern = null;
                        // Hilfsvariable, damit while-schleife mindestens einmal ausgeführt
                        // wird, da auch leere Eingabe erlaubt ist
                        boolean hasAsked = false;

                        while (!InputCheck.patternCheck(pattern) || !hasAsked) {
                            hasAsked = true;
                            System.out.print(COLOR_BLUE_BACKGROUND +
                                    "Enter title pattern (e.g. 'Harry Potter %'). Leave empty to print all items:"
                                    + COLOR_RESET);

                            pattern = scanner.nextLine();
                            // Erlaube auch leeres pattern als Eingabe, welches dann alle Items ausgibt
                            if (pattern.isEmpty()) {
                                break;
                            }
                        }
                        // Da dbService-Methode null-Wert ermöglicht, aber Scanner immer "" ausgibt,
                        // wird das hier umgewandelt
                        if (pattern == "") {
                            pattern = null;
                        }

                        List<Item> products = dbService.getProducts(pattern);

                        if (products != null && !products.isEmpty()) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Products matching the pattern:" + COLOR_RESET);
                            for (Item p : products) {
                                System.out.println(COLOR_CYAN + "ASIN: " + COLOR_RESET + p.getAsin() + COLOR_CYAN
                                        + ", Title: " + COLOR_RESET + p.getTitle());
                            }
                        } else {
                            System.out.println(COLOR_BLUE_BACKGROUND + "No products found matching the pattern: "
                                    + pattern + COLOR_RESET);
                        }
                        break;
                    }

                    // List<Item> getProductsByCategoryPath(String categoryPath);
                    case 3: {

                        // User-Eingabe lesen und speichern
                        System.out.println(COLOR_BLUE_BACKGROUND
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
                            System.out.println(COLOR_BLUE_BACKGROUND + COLOR_WHITE
                                    + "No items found for the given category path." + COLOR_RESET);
                        } else {
                            // Gib die gefundenen Items mit asin und titel aus
                            System.out.println(COLOR_BLUE_BACKGROUND + COLOR_WHITE + "Items found in category '"
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
                        System.out.println(COLOR_BLUE_BACKGROUND
                                + "Enter amount of top products (There are 721 Products with a perfect 5 star rating):"
                                + COLOR_RESET);
                        if (scanner.hasNextInt()) {
                            int k = scanner.nextInt();

                            List<Item> topProducts = dbService.getTopProducts(k);
                            if (topProducts != null && !topProducts.isEmpty()) {
                                System.out.println(COLOR_BLUE_BACKGROUND + "Top " + k + " products:" + COLOR_RESET);
                                for (Item product : topProducts) {
                                    System.out.println(COLOR_CYAN + "Average rating: " + COLOR_RESET
                                            + product.getAvg_review_score() + COLOR_CYAN + ", ASIN: " + COLOR_RESET
                                            + product.getAsin()
                                            + COLOR_CYAN + ", Title: " + COLOR_RESET + product.getTitle());
                                }
                            } else {
                                System.out.println(COLOR_BLUE_BACKGROUND + "No top products found." + COLOR_RESET);
                            }
                        } else {
                            // Falls kein Integer eingegeben wird
                            System.out.println(
                                    COLOR_RED_BACKGROUND + "Invalid input. Please enter a valid number." + COLOR_RESET);
                            scanner.nextLine(); //
                        }
                        break;
                    }

                    // List<Item> getSimilarCheaperProduct(String asin);
                    case 5: {
                        String asin = null;
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print(COLOR_BLUE_BACKGROUND
                                    + "Enter asin of product to search for similar cheaper products (e.g. 'B0000014HX'): "
                                    + COLOR_RESET);
                            asin = scanner.nextLine();
                        }
                        // Aufruf der methode aus dbService
                        List<Item> similarProducts = dbService.getSimilarCheaperProduct(asin);

                        if (similarProducts != null && !similarProducts.isEmpty()) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Similar products:" + COLOR_RESET);
                            for (Item product : similarProducts) {
                                System.out.println(COLOR_CYAN + "ASIN: " + COLOR_RESET + product.getAsin() + COLOR_CYAN
                                        + ", Title: " + COLOR_RESET + product.getTitle());
                            }
                        } else {
                            System.out.println(COLOR_BLUE_BACKGROUND + "No similar products found." + COLOR_RESET);
                        }
                        break;
                    }

                    // void addNewReview(String asin, int rating, int helpful, String reviewDate,
                    // int customerId, String summary, String content);
                    case 6: {
                        // check alles
                        String asin = null;
                        while (asin == null) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Enter asin: " + COLOR_RESET);
                            asin = scanner.nextLine();
                            if (!InputCheck.asinCheck(asin)) {
                                asin = null;
                            } else if (dbService.getProduct(asin) == null) {
                                System.out.println(COLOR_RED_BACKGROUND
                                        + "There is no product with this asin in the database. Please enter a valid asin!"
                                        + COLOR_RESET);
                                asin = null;
                            }
                        }

                        Integer rating = null;
                        String readRating = null;
                        while (rating == null) {

                            System.out.println(COLOR_BLUE_BACKGROUND + "Enter rating:" + COLOR_RESET);
                            readRating = scanner.nextLine();
                            if (!InputCheck.ratingCheck(readRating)) {
                                rating = null;
                            } else {
                                rating = Integer.parseInt(readRating);

                            }

                        }
                        // helpful gibt an, wie oft andere Nutzer die Rezension als hilfreich bewertet
                        // haben. Da macht es keinen Sinn, das mit einem anderen Wert als 0 zu
                        // initialisieren
                        Integer helpful = 0;
                        /*
                         * System.out
                         * .println(COLOR_BLUE_BACKGROUND + "Enter helpful (leave empty for null):" +
                         * COLOR_RESET);
                         * String helpful_input = scanner.nextLine(); // Nimm die Eingabe als String
                         * // Überprüfen, ob die Eingabe leer ist (d.h. der Benutzer möchte "null")
                         * if (helpful_input.isEmpty()) {
                         * helpful = null; // helpful bleibt null
                         * } else {
                         * try {
                         * // Versuche, die Eingabe in eine Zahl umzuwandeln
                         * helpful = Integer.parseInt(helpful_input);
                         * } catch (NumberFormatException e) {
                         * // Ungültige Eingabe (keine Zahl)
                         * System.out.println(COLOR_RED_BACKGROUND
                         * + "Invalid input. Please enter a valid number or leave it empty for null."
                         * + COLOR_RESET);
                         * }
                         * }
                         */

                        String reviewDate = java.time.LocalDate.now().toString();
                        System.out.println(COLOR_BLUE_BACKGROUND + "Today is " + reviewDate
                                + ". This date will be added to the review." + COLOR_RESET);

                        Integer customerId = -1;
                        while (customerId == -1) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Enter customerId: " + COLOR_RESET);
                            String customerid_input = scanner.nextLine();
                            if (customerid_input.isEmpty()) {
                                System.out.println(COLOR_RED_BACKGROUND
                                        + "Please enter a valid customerId!"
                                        + COLOR_RESET);
                                customerId = -1;
                            } else {
                                try {
                                    customerId = Integer.parseInt(customerid_input);
                                    if (dbService.getCustomer(customerId) == null) {
                                        System.out.println(COLOR_RED_BACKGROUND
                                                + "There is no customer with this ID in the database. Please enter a valid customerId!"
                                                + COLOR_RESET);
                                        customerId = -1;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println(COLOR_RED_BACKGROUND
                                            + "Invalid input. Please enter a valid number."
                                            + COLOR_RESET);
                                }
                            }
                        }

                        String summary = null;
                        while (!InputCheck.summaryCheck(summary)) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Enter review summary:" + COLOR_RESET);
                            summary = scanner.nextLine();
                        }
                        String content = null;
                        while (!InputCheck.summaryCheck(content)) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Enter review content:" + COLOR_RESET);
                            content = scanner.nextLine();
                        }

                        dbService.addNewReview(asin, rating, helpful, reviewDate, customerId, summary, content);
                        System.out.println(COLOR_BLUE_BACKGROUND + "Review added successfully." + COLOR_RESET);
                        break;
                    }

                    // List<ProductReviews> showReviews(String asin);
                    case 7: {
                        String asin = null;
                        // check asin
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print(COLOR_BLUE_BACKGROUND + "Enter asin of the item to see the reviews: "
                                    + COLOR_RESET);
                            asin = scanner.nextLine();
                        }
                        // dann weiter mit der asin
                        // Aufruf der dbService-Methode
                        List<ProductReviews> reviews = dbService.showReviews(asin);
                        if (reviews != null && !reviews.isEmpty()) {
                            System.out.println(COLOR_BLUE_BACKGROUND + "Reviews for " + asin + ":" + COLOR_RESET);
                            for (ProductReviews review : reviews) {
                                System.out.println(
                                        COLOR_CYAN + "-----------------------------------------------" + COLOR_RESET);
                                System.out.println(COLOR_CYAN + "Rating: " + COLOR_RESET + review.getRating());
                                System.out.println(COLOR_CYAN + "Helpful: " + COLOR_RESET + review.getHelpful());
                                System.out.println(COLOR_CYAN + "Review Date: " + COLOR_RESET + review.getReviewDate());
                                System.out.println(COLOR_CYAN + "Customer ID: " + COLOR_RESET + review.getCustomerId());
                                System.out.println(COLOR_CYAN + "Summary: " + COLOR_RESET + review.getSummary());
                                System.out.println(COLOR_CYAN + "Content: " + COLOR_RESET + review.getContent());
                            }
                        } else {
                            System.out.println(COLOR_BLUE_BACKGROUND + "No reviews found for " + asin + COLOR_RESET);
                        }
                        break;
                    }

                    // List<Customer>getTrolls(double averageRating);
                    case 8: {
                        Double averageRating = null;
                        System.out.println(COLOR_BLUE_BACKGROUND + "Enter average rating:" + COLOR_RESET);
                        if (scanner.hasNextDouble()) {
                            averageRating = scanner.nextDouble();
                            scanner.nextLine();
                            // Überprüfen, dass Eingabe ein double-Wert zwischen 1 und 5 ist
                            if (InputCheck.trollsCheck(averageRating)) {
                                // Aufruf der dbService Methode
                                List<Customer> trolls = dbService.getTrolls(averageRating);
                                if (trolls != null && !trolls.isEmpty()) {
                                    System.out.println(COLOR_BLUE_BACKGROUND + "Trolls with average rating of "
                                            + averageRating + ":" + COLOR_RESET);
                                    for (Customer troll : trolls) {
                                        System.out.println(COLOR_CYAN + "Customer ID: " + COLOR_RESET + troll.getId());
                                        System.out.println(COLOR_CYAN + "Name: " + COLOR_RESET + troll.getUsername());
                                        System.out.println(COLOR_CYAN + "Iban: " + COLOR_RESET + troll.getIban());
                                        System.out.println(COLOR_CYAN
                                                + "-----------------------------------------------" + COLOR_RESET);
                                    }
                                } else {
                                    System.out.println(COLOR_BLUE_BACKGROUND + "No trolls found with average rating of "
                                            + averageRating + COLOR_RESET);
                                }
                            } else {
                                //
                            }
                        } else {
                            System.out.println(
                                    COLOR_RED_BACKGROUND + "Invalid input. Please enter a valid double between 1 and 5."
                                            + COLOR_RESET);
                        }

                        break;
                    }

                    // List<Price>getOffers(String asin);
                    case 9: {
                        String asin = null;
                        // check asin
                        while (!InputCheck.asinCheck(asin)) {
                            System.out.print(COLOR_BLUE_BACKGROUND
                                    + "Enter asin to get the offers (e.g. 'B000000WMJ'): " + COLOR_RESET);
                            asin = scanner.nextLine();
                        }
                        // dann weiter mit der asin
                        // Aufruf der dbService-Methode
                        List<Price> offers = dbService.getOffers(asin);
                        // Überprüfe, ob es überhaupt Angebote gibt
                        if (offers != null && !offers.isEmpty()) {
                            // Falls ja, gebe alle Infos zu den Angeboten aus (Preis wird aus value und mult
                            // berechnet)
                            System.out.println(COLOR_BLUE_BACKGROUND + "Offers for " + asin + ":" + COLOR_RESET);
                            for (Price offer : offers) {
                                System.out.println(COLOR_CYAN + "Shop: " + COLOR_RESET + offer.getPrice_shop_id()
                                        + COLOR_CYAN
                                        + ", Preis: " + COLOR_RESET +
                                        +offer.getPrice_mult() * offer.getPrice_value() + offer.getPrice_currency()
                                        + COLOR_CYAN + ", Zustand: " + COLOR_RESET + offer.getPrice_state());

                                System.out.println(
                                        COLOR_CYAN + "-----------------------------------------------" + COLOR_RESET);
                            }
                        } else {
                            System.out.println(COLOR_BLUE_BACKGROUND + "No offers found for " + asin + COLOR_RESET);
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

                    default:
                        System.out.println(
                                COLOR_RED + "This choice doesn't exist (yet). Please try again." + COLOR_RESET);
                        break;

                }

            } else {
                // Handle non-integer input
                System.out.println(COLOR_RED_BACKGROUND + "Invalid input. Please enter a valid number." + COLOR_RESET);
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
