package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_reviews")
public class ProductReviews {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Ensures Hibernate uses the auto-increment functionality
	@Column(name = "review_id")
	private Integer id;
	@Column(name = "asin")
	private String asin;
	@Column(name = "rating")
	private Integer rating;
	@Column(name = "helpful")
	private Integer helpful;
	@Column(name = "reviewdate")
	private String reviewDate;
	@Column(name = "customer_id")
	private Integer customerId;
	@Column(name = "summary")
	private String summary;
	@Column(name = "review_content", length = 3677)
	private String content;

	public ProductReviews() {

	}

	public ProductReviews(String asin, Integer rating, Integer helpful, String reviewDate, Integer customerId,
			String summary,
			String content) {
		this.asin = asin;
		this.rating = rating;
		this.helpful = helpful;
		this.reviewDate = reviewDate;
		this.customerId = customerId;
		this.summary = summary;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getHelpful() {
		return helpful;
	}

	public void setHelpful(Integer helpful) {
		this.helpful = helpful;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
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
