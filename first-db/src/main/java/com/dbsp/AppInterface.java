package com.dbsp;

import java.util.List;

import com.dbsp.entity.Customer;
import com.dbsp.entity.Item;
import com.dbsp.entity.Price;
import com.dbsp.entity.ProductReviews;
import com.dbsp.extra.Category;

public interface AppInterface {

    void init(); // Initialisierungsmethode

    void finish();

    Item getProduct(String asin);

    List<Item> getProducts(String pattern);

    Category getCategoryTree();

    List<Item> getProductsByCategoryPath(String categoryPath);

    List<Item> getTopProducts(int k); // Items unter den Top k Ratings

    List<Item> getSimilarCheaperProduct(String asin);

    // unsicher wegen dem Review Ding!
    void addNewReview(String asin, Integer rating, Integer helpful, String reviewDate, Integer customerId,
            String summary, String content);

    List<ProductReviews> showReviews(String asin);

    List<Customer> getTrolls(double averageRating);

    List<Price> getOffers(String asin);
    // extra Methoden

    void addShop(String name, String street, int zip);

    // hier alle methoden rein, die in Aufgabenstellung sind
}
