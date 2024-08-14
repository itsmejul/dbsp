package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_reviews")
public class ProductReviews {

	@Id
	@Column(name="review_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="rating")
	private int rating;
	@Column(name="helpful")
	private int helpful;
	@Column(name="reviewdate")
	private String reviewDate;
	@Column(name="customer_id")
	private int customerId;
	@Column(name="summary")
	private String summary;
	@Column(name="review_content")
	private String content;
	
	public ProductReviews() {
		
	}
}
