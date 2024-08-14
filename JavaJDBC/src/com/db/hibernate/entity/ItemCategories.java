package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="item_categories")
public class ItemCategories {

	@Id
	@Column(name="asin")
	private String asin;
	@Column(name="category_id")
	private int categoryId;
	
	public ItemCategories() {
		
	}

	public ItemCategories(String asin, int categoryId) {
		this.asin = asin;
		this.categoryId = categoryId;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
