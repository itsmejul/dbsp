package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class ArtistId implements Serializable {

    private String name;
    private String asin;

    // Default constructor
    public ArtistId() {
    }

    // Constructor
    public ArtistId(String name, String asin) {
        this.name = name;
        this.asin = asin;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    // equals() method to compare two ArtistId objects
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArtistId artistId = (ArtistId) o;
        return Objects.equals(name, artistId.name) &&
                Objects.equals(asin, artistId.asin);
    }

    // hashCode() method to generate a hash based on name and asin
    @Override
    public int hashCode() {
        return Objects.hash(name, asin);
    }
}
