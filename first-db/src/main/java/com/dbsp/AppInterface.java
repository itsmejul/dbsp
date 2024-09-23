package com.dbsp;

import java.util.List;

import com.dbsp.entity.Customer;
import com.dbsp.entity.Item;
import com.dbsp.entity.Price;
import com.dbsp.entity.ProductReviews;
import com.dbsp.extra.Category;

public interface AppInterface {

    void init();

    void finish();

    Item getProduct(String asin);

    List<Item> getProducts(String pattern);

    Category getCategoryTree();

    List<Item> getProductsByCategoryPath(String categoryPath);

    List<Item> getTopProducts(int k);

    List<Item> getSimilarCheaperProduct(String asin);

    void addNewReview(String asin, Integer rating, Integer helpful, String reviewDate, Integer customerId,
            String summary, String content);

    List<ProductReviews> showReviews(String asin);

    List<Customer> getTrolls(double averageRating);

    List<Price> getOffers(String asin);

}
