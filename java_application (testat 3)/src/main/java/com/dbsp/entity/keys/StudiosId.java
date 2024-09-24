package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class StudiosId implements Serializable {

    private String name;
    private String asin;

    public StudiosId() {
    }

    public StudiosId(String name, String asin) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StudiosId studiosId = (StudiosId) o;
        return Objects.equals(name, studiosId.name) &&
                Objects.equals(asin, studiosId.asin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, asin);
    }
}
