package com.dbsp.entity;

import com.dbsp.entity.keys.ItemCategoryId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_categories")
@IdClass(ItemCategoryId.class) // Zusammengesetzer PK aus zwei Werte muss in extra Klasse

public class ItemCategories {

	@Id
	@Column(name = "asin")
	private String asin;

	@Id
	@Column(name = "category_id")
	private Integer categoryId;

	public ItemCategories() {

	}

	public ItemCategories(String asin, Integer categoryId) {
		this.asin = asin;
		this.categoryId = categoryId;
	}

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

}
