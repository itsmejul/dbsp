package com.dbsp;

import java.util.List;
import java.util.Scanner;

import com.dbsp.entity.Customer;
import com.dbsp.entity.Item;
import com.dbsp.entity.Price;
import com.dbsp.entity.ProductReviews;

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
        System.out.println("5. getTopProducts");
        System.out.println("6. getSimilarCheaperProduct");
        System.out.println("7. addNewReview");
        System.out.println("8. showReviews");
        System.out.println("9. getTrolls");
        System.out.println("10. getOffers");

        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {

                    //void finish();
                    case 0: {
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

                    //Item getProduct(String asin);
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

                    //List<Item> getProducts(String pattern);
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

                    //List<Item> getProductsByCategoryPath(String categoryPath);
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

                    //List<Item> getTopProducts(int k);
                    case 5: {
                        System.out.println("Enter amount of top products:");
                        int k = scanner.nextInt();
                        List<Item> topProducts = dbService.getTopProducts(k);
                        if (topProducts != null && !topProducts.isEmpty()) {
                            System.out.println("Top " + k + " products:");
                            for (Item product : topProducts) {
                                System.out.println("ASIN: " + product.getAsin() + ", Title: " + product.getTitle());
                            }
                        } else {
                            System.out.println("No top products found.");
                        }
                    }

                    //List<Item> getSimilarCheaperProduct(String asin);
                    case 6: {
                        System.out.println("Enter asin:");
                        String asin = scanner.nextLine();
                        List<Item> similarProducts = dbService.getSimilarCheaperProduct(asin);
                        if (similarProducts != null && !similarProducts.isEmpty()) {
                            System.out.println("Similar products:");
                            for (Item product : similarProducts) {
                                System.out.println("ASIN: " + product.getAsin() + ", Title: " + product.getTitle());
                            }
                        } else {
                            System.out.println("No similar products found.");
                        }
                    }

                    //void addNewReview(String asin, int rating, int helpful, String reviewDate, int customerId, String summary, String content);
                    case 7: {
                        System.out.println("Enter asin:");
                        String asin = scanner.nextLine();
                        System.out.println("Enter rating:");
                        int rating = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter helpful:");
                        int helpful = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter reviewDate:");
                        String reviewDate = scanner.nextLine();
                        System.out.println("Enter customerId:");
                        int customerId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter summary:");
                        String summary = scanner.nextLine();
                        System.out.println("Enter content:");
                        String content = scanner.nextLine();
                        dbService.addNewReview(asin, rating, helpful, reviewDate, customerId, summary, content);
                        System.out.println("Review added successfully.");
                    }

                    //List<ProductReviews> showReviews(String asin);
                    case 8: {
                        System.out.println("Enter asin:");
                        String asin = scanner.nextLine();
                        List<ProductReviews> reviews = dbService.showReviews(asin);
                        if (reviews != null && !reviews.isEmpty()) {
                            System.out.println("Reviews for " + asin + ":");
                            for (ProductReviews review : reviews) {
                                System.out.println("Rating: " + review.getRating());
                                System.out.println("Helpful: " + review.getHelpful());
                                System.out.println("Review Date: " + review.getReviewDate());
                                System.out.println("Customer ID: " + review.getCustomerId());
                                System.out.println("Summary: " + review.getSummary());
                                System.out.println("Content: " + review.getContent());
                                System.out.println("-----------------------------------------------");
                            }
                        } else {
                            System.out.println("No reviews found for " + asin);
                        }
                    }

                    //List<Customer>getTrolls(double averageRating);
                    case 9: {
                        System.out.println("Enter average rating:");
                        double averageRating = scanner.nextDouble();
                        scanner.nextLine();
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
                    }

                    //List<Price>getOffers(String asin);
                    case 10: {
                        System.out.println("Enter asin:");
                        String asin = scanner.nextLine();
                        List<Price> offers = dbService.getOffers(asin);
                        if (offers != null && !offers.isEmpty()) {
                            System.out.println("Offers for " + asin + ":");
                            for (Price offer : offers) {
                                System.out.println("Price State: " + offer.getPrice_state()+ " Price Value: " + offer.getPrice_value() + " Price Mult: " + offer.getPrice_mult() + " Price Currency: " + offer.getPrice_currency());
                                System.out.println("at Shop: " + offer.getPrice_shop_id());
                                System.out.println("-----------------------------------------------");
                            }
                        } else {
                            System.out.println("No offers found for " + asin);
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
