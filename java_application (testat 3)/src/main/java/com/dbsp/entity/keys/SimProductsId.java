package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class SimProductsId implements Serializable {

    private String asin_original;
    private String asin_similar;

    // Default constructor
    public SimProductsId() {
    }

    // Parameterized constructor
    public SimProductsId(String asin_original, String asin_similar) {
        this.asin_original = asin_original;
        this.asin_similar = asin_similar;
    }

    // Getters and setters
    public String getAsin_original() {
        return asin_original;
    }

    public void setAsin_original(String asin_original) {
        this.asin_original = asin_original;
    }

    public String getAsin_similar() {
        return asin_similar;
    }

    public void setAsin_similar(String asin_similar) {
        this.asin_similar = asin_similar;
    }

    // Override equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SimProductsId that = (SimProductsId) o;
        return Objects.equals(asin_original, that.asin_original) &&
                Objects.equals(asin_similar, that.asin_similar);
    }

    // Override hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(asin_original, asin_similar);
    }
}
