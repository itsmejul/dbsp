package com.dbsp;

import com.dbsp.entity.*;

import java.util.List;
import java.util.Properties;

public interface AppInterface {
    void init(); // Initialisierungsmethode

    void finish();

    Item getProduct(String asin);

    List<Item> getProducts(String pattern);

    List<Item> getProductsByCategoryPath(String categoryPath);

    void addShop(String name, String street, int zip);

    // hier alle methoden rein, die in Aufgabenstellung sind
}
