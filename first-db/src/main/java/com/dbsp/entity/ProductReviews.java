package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

	public ProductReviews(String asin, int rating, int helpful, String reviewDate, int customerId, String summary,
			String content) {
		this.asin = asin;
		this.rating = rating;
		this.helpful = helpful;
		this.reviewDate = reviewDate;
		this.customerId = customerId;
		this.summary = summary;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getHelpful() {
		return helpful;
	}

	public void setHelpful(int helpful) {
		this.helpful = helpful;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
