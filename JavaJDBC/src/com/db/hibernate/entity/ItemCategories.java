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
}
