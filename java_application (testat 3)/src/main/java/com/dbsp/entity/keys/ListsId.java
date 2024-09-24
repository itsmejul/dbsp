package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class ListsId implements Serializable {

    private String listname;
    private String asin;

    public ListsId() {
    }

    public ListsId(String listname, String asin) {
        this.listname = listname;
        this.asin = asin;
    }

    // Getters and setters
    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
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
        ListsId listsId = (ListsId) o;
        return Objects.equals(listname, listsId.listname) &&
                Objects.equals(asin, listsId.asin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listname, asin);
    }
}
