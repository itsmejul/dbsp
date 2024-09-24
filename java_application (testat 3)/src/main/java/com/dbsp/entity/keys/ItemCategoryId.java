package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

// Define the composite key class
public class ItemCategoryId implements Serializable {

    private String asin;
    private Integer categoryId;

    // Default constructor
    public ItemCategoryId() {
    }

    // Constructor
    public ItemCategoryId(String asin, Integer categoryId) {
        this.asin = asin;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemCategoryId that = (ItemCategoryId) o;
        return categoryId == that.categoryId && Objects.equals(asin, that.asin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asin, categoryId);
    }
}
