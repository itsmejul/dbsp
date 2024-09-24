package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class TracksId implements Serializable {

    private String title;
    private String asin;

    public TracksId() {
    }

    public TracksId(String title, String asin) {
        this.title = title;
        this.asin = asin;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TracksId tracksId = (TracksId) o;
        return Objects.equals(title, tracksId.title) &&
                Objects.equals(asin, tracksId.asin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, asin);
    }
}
